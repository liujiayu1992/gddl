import com.zhiren.webservice.ruchy.HuayInterFace;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by liuzhiyu on 2017/3/29.
 */
public class InterFaceRucServiceTest {
    public static void main(String args[]) {
        byte[] b = getBytesFromFile(getXMLFile());
        InterFaceRucService ifs = new InterFaceRucService();
        ifs.syncdata("","",b,"CZS");
    }

    /**
     * 读取xml文件
     *
     * @Author
     */
    private static File getXMLFile() {
        String path = System.getProperty("user.dir");
        File f = new File(path + "/data.xml");// CAG1605110199
        return f;
    }

    /**
     * 文件转化为字节数组
     *
     * @Author
     */
    private static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1024];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}