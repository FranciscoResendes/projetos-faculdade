package pt.ul.fc.css.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.handlers.StatisticsHandler;

@Service
public class StatisticsService {

  @Autowired StatisticsHandler statHandler;

  public StatisticsService(StatisticsHandler handler) {
    this.statHandler = handler;
  }

  public float getStatistics() {
    return this.statHandler.calculateStatistics();
  }
}
