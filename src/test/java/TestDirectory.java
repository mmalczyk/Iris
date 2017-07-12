import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Magda on 04/07/2017.
 */
class TestDirectory {
    static final Path results = FileSystems.getDefault().getPath("./src\\test\\resultImages");

    static final Path images = FileSystems.getDefault().getPath("./src\\test\\testImages");
}
