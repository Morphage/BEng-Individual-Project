/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.practice.views;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

/**
 *
 * @author achantreau
 */
public class BinaryTree extends StackPane {

    final WebView webview = new WebView();

    private String json;

    private static final String htmlTemplate
            = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
            + "\n"
            + "<!-- CSS Files -->\n"
            + "<link type=\"text/css\" href=\"http://www.doc.ic.ac.uk/~ac6609/css/base.css\" rel=\"stylesheet\" />\n"
            + "<link type=\"text/css\" href=\"http://www.doc.ic.ac.uk/~ac6609/css/Spacetree.css\" rel=\"stylesheet\" />\n"
            + "\n"
            + "<!--[if IE]><script language=\"javascript\" type=\"text/javascript\" src=\"../../Extras/excanvas.js\"></script><![endif]-->\n"
            + "\n"
            + "<!-- JIT Library File -->\n"
            + "<script language=\"javascript\" type=\"text/javascript\" src=\"http://www.doc.ic.ac.uk/~ac6609/js/jit.js\"></script>\n"
            + "\n"
            + "<!-- Example File -->\n"
            + "<script language=\"javascript\" type=\"text/javascript\" src=\"http://www.doc.ic.ac.uk/~ac6609/js/binary_tree.js\"></script>\n"
            + "<script language=\"javascript\" type=\"text/javascript\">\n"
            + "var json = ${json}\n"
            + "</script>\n"
            + "\n"
            + "</head>\n"
            + "\n"
            + "<body onload=\"init(json);\">\n"
            + "<div id=\"container\">\n"
            + "\n"
            + "<div id=\"center-container\">\n"
            + "    <div id=\"infovis\"></div>    \n"
            + "</div>\n"
            + "\n"
            + "</body>\n"
            + "</html>";

    private String applyEditingTemplate() {
        return htmlTemplate.replace("${json}", json);
    }

    public void setJSON(String newCode) {
        this.json = newCode;
        webview.getEngine().loadContent(applyEditingTemplate());
    }

    public BinaryTree(String json) {
        this.json = json;

        webview.setMinSize(600, 550);
        webview.setPrefSize(600, 550);
        
        webview.getEngine().loadContent(applyEditingTemplate());

        this.getChildren().add(webview);
    }
}
