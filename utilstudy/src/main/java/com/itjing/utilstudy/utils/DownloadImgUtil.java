package com.itjing.utilstudy.utils;

import org.springframework.util.ObjectUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年08月20日 13:14
 * @Description: 根据链接地址批量下载图片
 */
public class DownloadImgUtil {

    public static void main(String[] args) {
        String[] urlArr = {
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048267685-7d849306-2fc6-43b5-9be1-c543afdd29c0.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048324032-f9dc421a-9ca9-4a2c-8f82-aa03080c0e5d.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048447492-9eddf35c-0fda-4dc7-9e4e-3f44b1a31e31.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048470727-631f7306-4631-4a0b-83f8-784e904de4ca.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048480715-69bf874a-2bb6-44e6-95ad-47e4d5d4856e.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048602714-83d1e289-82d1-4bd8-ae6a-dc338afda705.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048631688-4e21c91c-78aa-40da-8a1c-ad963c0611bc.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048718375-fa5dcbae-2fdd-4f35-ad15-8adb60e16c94.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048761053-ada3fe6b-e858-4868-bee0-cab84759141c.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048774450-bf77fd6d-0a15-4c86-be09-cb3962764650.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048829596-0c17a4a4-b777-4807-85e8-b6bc4cc6bb9a.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048862286-76b8c2d6-0a70-43a2-80ab-7f0ffcfdfd4b.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626048883858-e3bb9acd-70d8-4abb-87e6-b151a9002bc2.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626049183241-2f5c0607-3c59-412c-8969-5507bf9be206.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051218730-72317779-16ae-40e8-ab9b-7b74996dd967.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051302126-f9cbd09d-0eab-413f-b139-fa265ba0ab25.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051361351-0e3e453f-3835-4017-8dde-f8749fa5ac19.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051429593-06242eb5-a1a2-4bba-b699-15c09dc35600.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051450620-26610f05-655e-4fcb-a245-39925cc6fbb8.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051470418-fbe9093d-5fc4-4492-9e2a-5bae540ecce4.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051486874-41ec57bb-b0fa-433c-b81e-18d0021e65ff.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051508841-75227d05-c95e-41a9-93ec-64ced422d582.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051593869-00b46c2f-58a1-4e54-93ee-59efdbe538d4.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051614062-28c3675d-3b9c-4d12-9097-bfc47b0b46ce.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051632996-1ed2555b-c3a0-4da3-97e5-9c02df7ce9f4.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051665157-47f15d00-c9d0-4c18-b780-f43e5df6e656.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051685541-9dc58521-338e-47a3-bfe9-c1f56bf9d0d5.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051703777-5c4667b4-892f-4144-830f-75f8094e1ad6.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051729696-26fa9568-8758-4d95-9a04-8a16e6c09f46.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051782821-4b548929-fdfc-450f-ad32-19f126aed3f9.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051807064-417f1f7c-ed1e-4376-a6e9-15c1af612ccd.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052010676-add3c101-e0cc-4fba-8169-65321cacf4f7.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052249017-cdc15728-01b0-4c3d-956a-91fbe55baab7.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052270213-e827b23c-33b0-40dd-a9b6-16f67906657a.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052291425-d2d4ef09-0952-4b22-9427-3315ecc974de.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052318890-dd2c1db9-0b75-46c5-9ff3-b16bfd5f077e.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052340399-08837cc9-4c89-4ec1-af66-1c2a1aaaaa4b.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052365770-6eb53b69-b7af-45f4-9d27-143573c7f36f.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052383166-0e04b89b-dc4a-488f-994c-205109a546a6.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052408644-e96c4457-2c43-4209-a9bf-260ded98b3eb.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052430216-b25f7b9a-cfe2-4bce-88b0-71b97edbd298.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052459630-faebc902-4674-46db-9102-d57b9bdd9834.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052478852-3d701384-f390-4c11-bddb-90fb14a58238.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626052646828-4d6abf5a-ac00-4d4e-982a-4d6a76ff03ca.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053022944-bc7b7c9b-b7fc-47d6-b0d8-798ed4f17e21.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053053203-a1bb57f0-3ce8-41dd-ac93-aa552d78d296.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053066784-87225444-53b3-420b-943a-ec5bd1bfddf3.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053091056-67602563-f71f-4142-bd05-16192c201729.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053111968-75105db5-6270-4e8c-8056-19ee41f2729d.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626053129822-76298dc7-3152-4f4a-a26c-3b7f8c963c73.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051729696-26fa9568-8758-4d95-9a04-8a16e6c09f46.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051782821-4b548929-fdfc-450f-ad32-19f126aed3f9.png",
                "https://cdn.nlark.com/yuque/0/2021/png/12437945/1626051807064-417f1f7c-ed1e-4376-a6e9-15c1af612ccd.png"
        };

        download(Arrays.asList(urlArr));
    }

    public static void download(List<String> urls) {
        for (String imgUrl : urls) {
            if (ObjectUtils.isEmpty(imgUrl)) {
                continue;
            }
            try {
                String fileName = imgUrl.substring(imgUrl.lastIndexOf("/"));
                String savePath = "E:\\workspace_idea\\ComponentStudy\\utilstudy\\download\\";
                // 构造URL
                URL url = new URL(imgUrl);
                // 打开连接
                URLConnection con = url.openConnection();
                con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
                //设置请求超时为5s
                con.setConnectTimeout(5 * 1000);
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                File sf = new File(savePath);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                long length = (sf.length()) / 128;
                OutputStream os = new FileOutputStream(sf.getPath() + "/" + fileName);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
                //urls.add(imgUrl);
                continue;
            }
        }

        System.out.println("===================end====================");
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {

        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


}
