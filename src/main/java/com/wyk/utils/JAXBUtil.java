package com.wyk.utils;

/**
 * @author yunqiu
 * @date 2019/7/21
 */
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class JAXBUtil {

    private static final Logger log = Logger.getLogger("JAXBUtil.class");

    /**
     * 将JAXB标记的JavaBean转换成xml字符串
     * @return
     * @throws Exception
     */
    public static String bean2xml(Object obj){
        if(obj==null) return null;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(obj.getClass());
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);//是否格式化生成的xml
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mar.setProperty(Marshaller.JAXB_FRAGMENT,false);// 是否省略xml头声明信息

            StringWriter sw = new StringWriter();
            mar.marshal(obj, sw);

            String xmlString = sw.toString();
            log.info(obj.getClass().getSimpleName()+"生成的xml字符串---->"+xmlString);

            return xmlString;
        } catch (JAXBException e) {
            log.error(obj.getClass().getSimpleName()+"转换xml失败",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * xml转换成JavaBean
     * @param xml
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T xml2bean(String xml, Class<T> clazz){
        if(StringUtils.isEmpty(xml) || clazz==null) return null;
        log.info("xml2bean--->"+clazz.getSimpleName());

        JAXBContext context;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            log.error(xml+"转换JavaBean"+clazz.getSimpleName()+"失败",e);
            throw new RuntimeException(e);
        }
    }
}
