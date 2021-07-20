package top.bettercode.simpleframework.web.serializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.bettercode.logging.operation.PrettyPrintingContentModifier;
import top.bettercode.simpleframework.support.code.ICodeService;
import top.bettercode.simpleframework.web.DataDicBean;

/**
 * @author Peter Wu
 * @since 0.1.15
 */
public class BigDecimalSerializerTest {

  @BeforeEach
  public void setUp() {
    CodeSerializer.setCodeService(new ICodeService() {
      @Override
      public String getName(String codeType, Serializable code) {
        return "codeName";
      }

      @Override
      public Serializable getCode(String codeType, String name) {
        return 123;
      }
    });

  }

  @Test
  public void serializeBigDecimal() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);
    objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

    DataDicBean dicBean = new DataDicBean();
    dicBean.setNumber1(new BigDecimal("1.234560"));
    dicBean.setNumber2(new BigDecimal("1.23456"));
    dicBean.setNumber22(new BigDecimal("1.23456"));
    dicBean.setNumber3(new BigDecimal("1.00000"));
    dicBean.setNumber4(new BigDecimal("1.00000"));
    String string = objectMapper.writeValueAsString(dicBean);
    System.err.println(PrettyPrintingContentModifier.modifyContent(string));
    dicBean = new DataDicBean();
    dicBean.setNumber1(new BigDecimal("1.234560"));
    dicBean.setNumber2(new BigDecimal("1.20000"));
    dicBean.setNumber22(new BigDecimal("1.20000"));
    dicBean.setNumber3(new BigDecimal("1.010"));
    dicBean.setNumber4(new BigDecimal("1.010"));
    string = objectMapper.writeValueAsString(dicBean);
    System.err.println(PrettyPrintingContentModifier.modifyContent(string));
  }

}