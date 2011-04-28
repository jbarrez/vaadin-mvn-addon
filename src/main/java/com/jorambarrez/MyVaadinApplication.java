package com.jorambarrez;

import org.vaadin.virkki.paperstack.PaperStack;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Joram Barrez
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {
  
  private static final String DISPLAYED_WORD = "ACTIVITI";

  private Window window;
  private Refresher refresher;
  private Button goButton;
  private PaperStack paperStack;

  @Override
  public void init() {
    window = new Window("My Vaadin Application");
    setMainWindow(window);
    
    initGoButton();
    initPaperStack();
  }
  
  private void initGoButton() {
    goButton = new Button("Flip to the end");
    window.addComponent(goButton);
    
    goButton.addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        goButton.setEnabled(false);
        startRefresher();
        startPageFlipThread();
      }
    });
  }
  
  private void startRefresher() {
    refresher = new Refresher();
    window.addComponent(refresher);
    refresher.setRefreshInterval(100L);
  }
  
  private void startPageFlipThread() {
    Thread thread = new Thread(new Runnable() {
      public void run() {
        goButton.setEnabled(false);
        int nrOfUpdates = DISPLAYED_WORD.length() - 1;
        while (nrOfUpdates >= 0) {
          paperStack.navigate(true);
          nrOfUpdates--;
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        
        // Remove refresher when done (for performance)
        goButton.setEnabled(true);
        window.removeComponent(refresher);
        refresher = null;
      }
    });
    thread.start();
  }
  
  private void initPaperStack() {
    paperStack = new PaperStack();
    window.addComponent(paperStack);
    
    for (int i=0; i<DISPLAYED_WORD.length(); i++) {
      VerticalLayout verticalLayout = new VerticalLayout();
      verticalLayout.setSizeFull();
      paperStack.addComponent(verticalLayout);
      
      // Quick-hack CSS since I'm to lazy to define a styles.css
      Label label = new Label("<div style=\"text-align:center;color:blue;font-weight:bold;font-size:100px;text-shadow: 5px 5px 0px #eee, 7px 7px 0px #707070;\">" + DISPLAYED_WORD.charAt(i) + "</div>", Label.CONTENT_XHTML);
      label.setWidth(100, Label.UNITS_PERCENTAGE);
      verticalLayout.addComponent(label);
      verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    }
  }
  
}
