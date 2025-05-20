package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.domainmodel.TraineeshipPosition;
import java.util.List;

public interface PositionsSearchStrategy {
    List<TraineeshipPosition> search(String applicantUsername);

}
