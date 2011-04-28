package com.jorambarrez;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

  private Window window;
  private Label label;
  private Refresher refresher;
  private Button countButton;

  @Override
  public void init() {
    
    window = new Window("My Vaadin Application");
    setMainWindow(window);

    label = new Label("ping");
    window.addComponent(label);
    
    refresher = new Refresher();
    window.addComponent(refresher);
    
    countButton = new Button("Start");
    window.addComponent(countButton);
    countButton.addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        refresher.setRefreshInterval(500L);
        Thread thread = new Thread(new PingPongRunnable());
        thread.start();
      }
    });
  }
  
  class PingPongRunnable implements Runnable {
    
    public void run() {
      while (true) {
        synchronized (MyVaadinApplication.this) {
          if (label.getValue().equals("ping")) {
            label.setValue("pong");
          } else {
            label.setValue("ping");
          }
        }
        
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      
    }
    
  }

}
