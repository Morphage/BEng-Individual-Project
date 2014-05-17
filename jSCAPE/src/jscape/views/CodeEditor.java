package jscape.views;

/**
 * All credit for this code goes to jewelsea. Source:
 * https://gist.github.com/jewelsea/1463485
 */
import javafx.scene.web.WebView;

/**
 * A syntax highlighting code editor for JavaFX created by wrapping a CodeMirror
 * code editor in a WebView.
 *
 * See http://codemirror.net for more information on using the codemirror
 * editor.
 */
public class CodeEditor {

    /**
     * a Webview used to encapsulate the CodeMirror JavaScript.
     */
    private static final WebView webview = new WebView();

    /**
     * a template for editing code - this can be changed to any template derived
     * from the supported modes at http://codemirror.net to allow syntax
     * highlighted editing of a wide variety of languages.
     */
    private static final String editingTemplate
            = "<!doctype html>"
            + "<html>"
            + "<head>"
            + "  <link rel=\"stylesheet\" href=\"http://codemirror.net/lib/codemirror.css\">"
            + "  <script src=\"http://codemirror.net/lib/codemirror.js\"></script>"
            + "  <script src=\"http://codemirror.net/mode/clike/clike.js\"></script>"
            + "</head>"
            + "<body>"
            + "<form><textarea id=\"code\" name=\"code\">\n"
            + "${code}"
            + "</textarea></form>"
            + "<script>"
            + "  var editor = CodeMirror.fromTextArea(document.getElementById(\"code\"), {"
            + "    lineNumbers: true,"
            + "    matchBrackets: true,"
            + "    mode: \"text/x-java\","
            + "    readOnly: true"
            + "  });editor.setSize(\"100%\", \"100%\");"
            + "</script>"
            + "</body>"
            + "</html>";

    public static void applyEditingTemplate(String editingCode) {
        webview.getEngine().loadContent(editingTemplate.replace("${code}", editingCode));
    }
    
    public static WebView getWebview() {
        return webview;
    }
}
