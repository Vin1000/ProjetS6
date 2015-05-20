package SearchUs.client.application.resultwidget;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vincent on 2015-05-20.
 */
public class FileExtension
{
    private static FileExtension ourInstance = new FileExtension();

    public static FileExtension getInstance() {
        return ourInstance;
    }

    private FileExtension()
    {
        dictionary.put("_blank", "icons/_blank.png");
        dictionary.put("aac", "icons/aac.png");
        dictionary.put("ai", "icons/ai.png");
        dictionary.put("aiff", "icons/aiff.png");
        dictionary.put("avi", "icons/avi.png");
        dictionary.put("bmp", "icons/bmp.png");
        dictionary.put("c", "icons/c.png");
        dictionary.put("cpp", "icons/cpp.png");
        dictionary.put("css", "icons/css.png");
        dictionary.put("dat", "icons/dat.png");
        dictionary.put("dmg", "icons/dmg.png");
        dictionary.put("doc", "icons/doc.png");
        dictionary.put("docx", "icons/doc.png");
        dictionary.put("dwg", "icons/dwg.png");
        dictionary.put("dxf", "icons/dxf.png");
        dictionary.put("eps", "icons/eps.png");
        dictionary.put("exe", "icons/exe.png");
        dictionary.put("flv", "icons/flv.png");
        dictionary.put("gif", "icons/gif.png");
        dictionary.put("h", "icons/h.png");
        dictionary.put("hpp", "icons/hpp.png");
        dictionary.put("html", "icons/html.png");
        dictionary.put("ics", "icons/ics.png");
        dictionary.put("iso", "icons/iso.png");
        dictionary.put("java", "icons/java.png");
        dictionary.put("jpg", "icons/jpg.png");
        dictionary.put("jpeg", "icons/jpg.png");
        dictionary.put("js", "icons/js.png");
        dictionary.put("key", "icons/key.png");
        dictionary.put("less", "icons/less.png");
        dictionary.put("mid", "icons/mid.png");
        dictionary.put("mp3", "icons/mp3.png");
        dictionary.put("mp4", "icons/mp4.png");
        dictionary.put("mpg", "icons/mpg.png");
        dictionary.put("mpeg", "icons/mpg.png");
        dictionary.put("odf", "icons/odf.png");
        dictionary.put("ods", "icons/ods.png");
        dictionary.put("odt", "icons/odt.png");
        dictionary.put("otp", "icons/otp.png");
        dictionary.put("ots", "icons/ots.png");
        dictionary.put("ott", "icons/ott.png");
        dictionary.put("page", "icons/page.png");
        dictionary.put("pdf", "icons/pdf.png");
        dictionary.put("php", "icons/php.png");
        dictionary.put("png", "icons/png.png");
        dictionary.put("ppt", "icons/ppt.png");
        dictionary.put("pptx", "icons/ppt.png");
        dictionary.put("psd", "icons/psd.png");
        dictionary.put("py", "icons/py.png");
        dictionary.put("qt", "icons/qt.png");
        dictionary.put("rar", "icons/rar.png");
        dictionary.put("rb", "icons/rb.png");
        dictionary.put("rtf", "icons/rtf.png");
        dictionary.put("sass", "icons/sass.png");
        dictionary.put("scss", "icons/scss.png");
        dictionary.put("sql", "icons/sql.png");
        dictionary.put("tga", "icons/tga.png");
        dictionary.put("tgz", "icons/tgz.png");
        dictionary.put("tiff", "icons/tiff.png");
        dictionary.put("txt", "icons/txt.png");
        dictionary.put("wav", "icons/wav.png");
        dictionary.put("xls", "icons/xls.png");
        dictionary.put("xlsx", "icons/xlsx.png");
        dictionary.put("xml", "icons/xml.png");
        dictionary.put("yml", "icons/yml.png");
        dictionary.put("zip", "icons/zip.png");
    }

    private Map<String, String> dictionary = new HashMap<String, String>();
    //"icons/" + ext + ".png"

    public String getIconUrl(String extension)
    {
        if(!dictionary.containsKey(extension))
        {
            return dictionary.get("_blank");
        }
        return dictionary.get(extension);
    }
}
