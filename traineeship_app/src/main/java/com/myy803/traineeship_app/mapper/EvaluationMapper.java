package com.myy803.traineeship_app.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myy803.traineeship_app.domainmodel.Evaluation;

@Repository
public interface EvaluationMapper extends JpaRepository<Evaluation, Integer> {
	List<Evaluation> findByTraineeshipPositionId(Integer id);
}
