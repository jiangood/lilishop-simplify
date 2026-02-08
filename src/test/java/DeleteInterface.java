import cn.hutool.core.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteInterface {

    public static void main(String[] args) {
        String serviceDir = "d:\\ws\\mall\\lilishop-simplify\\src\\main\\java";
        List<String> interfacesToDelete = new ArrayList<>();

        // 遍历所有Java文件
        traverseDirectory(new File(serviceDir), interfacesToDelete);

        // 输出结果
        System.out.println("找到的接口文件及其对应的Impl类文件:");
        for (String interfacePath : interfacesToDelete) {
            System.out.println("接口: " + interfacePath);
            String implPath = interfacePath.replace(".java", "Impl.java");
            System.out.println("Impl类: " + implPath);
            System.out.println();
        }

        // 输出要删除的接口文件列表
        System.out.println("\n要删除的接口文件:");
        for (String interfacePath : interfacesToDelete) {
            System.out.println(interfacePath);
        }



        // 执行删除操作
        int deletedCount = 0;
        for (String interfacePath : interfacesToDelete) {
            File interfaceFile = new File(interfacePath);
            if (interfaceFile.delete()) {
                deletedCount++;


                File implFile = getImplFile(interfaceFile);


                //public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

                String s = FileUtil.readUtf8String(implFile);
                String mainName = FileUtil.mainName(interfaceFile.getName());
                s = s.replace(mainName+"Impl ",mainName+" ")
                        .replace("implements " + mainName, "")
                        .replace("@Override","")
                ;

                FileUtil.writeUtf8String(s, implFile);

                implFile.renameTo(interfaceFile);



                System.out.println("已删除: " + interfacePath);
            } else {
                System.err.println("删除失败: " + interfacePath);
            }
        }



        System.out.println("\n总共找到 " + interfacesToDelete.size() + " 个需要删除的接口文件");
        System.out.println("成功删除 " + deletedCount + " 个接口文件");
    }

    private static void traverseDirectory(File directory, List<String> interfacesToDelete) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file, interfacesToDelete);
            } else if (file.getName().endsWith(".java")) {
                // 检查是否是接口文件
                if (isOk(file)) {
                    // 检查是否存在对应的Impl类文件
                    File implFile = getImplFile(file);
                    if (implFile.exists()) {
                        interfacesToDelete.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    @NotNull
    private static File getImplFile(File file) {
        String implFileName = file.getName().replace(".java", "Impl.java");
        File implFile = new File(file.getParent(), implFileName);
        return implFile;
    }

    private static boolean isOk(File file) {
        if(file.getName().contains("AbstractPromotionsService")){
            return false;
        }
        String str = FileUtil.readUtf8String(file);
        return  str.contains("public interface");
    }
}