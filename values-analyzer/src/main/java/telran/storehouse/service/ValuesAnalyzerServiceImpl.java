package telran.storehouse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.model.*;
import telran.storehouse.repo.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValuesAnalyzerServiceImpl implements ValuesAnalyzerService {

    final StreamBridge streamBridge;
    @Value("${app.values.analyzer.producer.binding.name}")
    private String producerBindingName;
    @Value("${app.values.analyzer.producer.error.count.binding.name}")
    private String producerErrorCountBindingName;
    @Value("${app.values.analyzer.producer.error.received.binding.name}")
    private String producerErrorReceivedBindingName;
    @Value("${app.reducing.size}")
    int reducingSize;
    @Value("${app.sensor.data.timeout.seconds}")
    private long checkInterval;
    final ErrorsCountRepo errorsCountRepo;
    final SensorTimeoutRepo sensorTimeoutRepo;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Value("${app.missed.sensor.data.max}")
    int maxMissedData;
    private volatile long lastDataReceivedTime;
    private volatile boolean dataReceived = false;
    private volatile SensorDataDto lastSensorData;

    @PostConstruct
    private void init() {
        SensorDataSchedule();
    }

    private void SensorDataSchedule() {
        scheduler.scheduleAtFixedRate(this::checkMissedData, checkInterval, checkInterval, TimeUnit.MINUTES);
    }

    @Override
    @Transactional
    public void sensorDataAnalyzing(SensorDataDto sensorData) {
        if (sensorData == null) {
            log.error("SensorDataDto is null");
            return;
        }
        log.trace("received {}", sensorData);
        long id = sensorData.id();
        double fullness = sensorData.fullness();
        if (fullness <= 100 && fullness >= 0) {
            log.debug("the fullness of the container with sensorId: {} corresponds to the normal value", sensorData.id());
            streamBridge.send(producerBindingName, sensorData);
            log.debug("sensor data {} sent to {}", sensorData, producerBindingName);
        } else {
            addErrorCount(sensorData);
            log.debug("added error count for sensorId: {}", id);
        }
        synchronized (this) {
            lastSensorData = sensorData;
            lastDataReceivedTime = System.currentTimeMillis();
            dataReceived = true;
        }
    }

    private void checkMissedData() {
        synchronized (this) {
            if (!dataReceived && lastSensorData != null && (System.currentTimeMillis() - lastDataReceivedTime) >= TimeUnit.MINUTES.toMillis(checkInterval)) {
                long id = lastSensorData.id();
                SensorTimeout timeout = sensorTimeoutRepo.findById(id).orElseGet(() -> new SensorTimeout(id, lastDataReceivedTime));
                List<Long> errorsTimestampList = timeout.getErrorsTimestamp() != null ? timeout.getErrorsTimestamp() : new ArrayList<>();
                errorsTimestampList.add(System.currentTimeMillis());
                if (errorsTimestampList.size() >= maxMissedData) {
                    streamBridge.send(producerErrorReceivedBindingName, timeout.build());
                    log.debug("SensorId: {} - error count sent to {}", id, producerErrorReceivedBindingName);
                    errorsTimestampList.clear();
                }
                timeout.setErrorsTimestamp(errorsTimestampList);
                sensorTimeoutRepo.save(timeout);
            }
            dataReceived = false;
        }
    }

    private void addErrorCount(SensorDataDto sensorData) {
        Long sensorId = sensorData.id();
        Double fullness = sensorData.fullness();
        ErrorCount errorCount = errorsCountRepo.findById(sensorId).orElseGet(() -> {
            log.debug("ErrorCount for sensorId {} is null, creating new ErrorCount", sensorId);
            return new ErrorCount(sensorId);
        });
        List<Double> errorsCounter = getOrInitializeErrorsCounter(errorCount);
        errorsCounter.add(fullness);
        if (errorsCounter.size() >= reducingSize) {
            streamBridge.send(producerErrorCountBindingName, errorCount.build());
            log.debug("SensorId: {} - error count sent to {}", sensorId, producerErrorCountBindingName);
            errorsCounter.clear();
        }
        errorsCountRepo.save(errorCount);
        log.debug("ErrorCount for sensorId {} has been saved", sensorId);
    }

    private List<Double> getOrInitializeErrorsCounter(ErrorCount errorCount) {
        List<Double> errorsCounter = errorCount.getErrorsCounter();
        if (errorsCounter == null) {
            errorsCounter = new ArrayList<>();
            errorCount.setErrorsCounter(errorsCounter);
            log.debug("ErrorsCounter list for sensorId {} is null, initializing new list", errorCount.getSensorId());
        }
        return errorsCounter;
    }
}