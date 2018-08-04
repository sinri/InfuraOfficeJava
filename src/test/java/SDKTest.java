import org.apache.commons.text.StringEscapeUtils;

public class SDKTest {
    public static void main(String[] argv) {
        String path = "/tmp/*";
        String command = "echo -e 'import glob\\nlist=glob.glob(\"" + StringEscapeUtils.escapeJava(path.trim()) + "\")\\nfor item in list:\\n\\tprint(item)'|python -;";
        System.out.println(command);
    }
}
