import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityTemplate;
import org.apache.hc.core5.http.message.HttpResponseWrapper;
import org.apache.hc.core5.http.protocol.RequestHandlerRegistry;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class CryptApi {
    public static void main(String[] args) throws NamingException {
        CryptApi cryptApi = new CryptApi();
        InitialContext context = new InitialContext();

    }

    class HttpClient {
        private CloseableHttpClient httpClient = HttpClients.createDefault();
        private String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";
        private String token = "token";

        public void createDocument(ProductDocument productDocument, String signature, ProductGroup prGroup ) throws IOException {
            Data sendData = new Data(productDocument, prGroup, signature );

            JSONObject jsonObject = new JSONObject(sendData);

            HttpEntity httpEntity = EntityBuilder.create()
                    .setContentType(ContentType.APPLICATION_JSON)
                    .setText(jsonObject.toString()).build();

            HttpPost httpPost = new HttpPost(url + "?pg=" + prGroup);
            httpPost.setHeader("Authorization", "Bearer " + token);
            httpPost.setEntity(httpEntity);

            HttpResponse response = httpClient.execute(httpPost);

            System.out.println(response.getCode());

        }

    }

     class Data {
        private DocumentFormat document_format;
        private String product_document;
        private ProductGroup product_group;
        private String signature;
        private DocumentType documentType;
        private Base64.Encoder encoder = Base64.getEncoder();

         public Data() {
         }

         public Data( ProductDocument productDocument, ProductGroup product_group, String signature) {
             this.document_format = productDocument.getDocumentType().getDocumentFormat();
             this.product_document = encoder.encodeToString(product_document.getBytes());
             this.product_group = product_group;
             this.signature = signature;
             this.documentType = productDocument.getDocumentType();
         }

         @Override
         public String toString() {
             return "Data{" +
                     "document_format=" + document_format +
                     ", product_document='" + product_document + '\'' +
                     ", product_group=" + product_group +
                     ", signature='" + signature + '\'' +
                     ", type=" + documentType +
                     '}';
         }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;
             Data data = (Data) o;
             return document_format == data.document_format && Objects.equals(product_document, data.product_document) && product_group == data.product_group && Objects.equals(signature, data.signature) && documentType == data.documentType;
         }

         @Override
         public int hashCode() {
             return Objects.hash(document_format, product_document, product_group, signature, documentType);
         }
     }


     enum ProductGroup {
        CLOTHES("clothes"),
         SHOES("shoes"),
         TOBACCO("tobacco"),
         PERFUMERY("perfumery"),
         TIRES("tires"),
         ELECTRONICS("electronics"),
         PHARMA("pharma"),
         MILK("milk"),
         BICYCLE("bicycle"),
         WHEELCHAIRS("wheelchairs");

         ProductGroup(String name) {
             this.name = name;
         }
         private String name;
         public String getName() {
             return name;
         }
     }
     enum DocumentType {
         AGGREGATION_DOCUMENT(DocumentFormat.MANUAL),
         AGGREGATION_DOCUMENT_CSV(DocumentFormat.CSV),
         AGGREGATION_DOCUMENT_XML(DocumentFormat.XML),
         DISAGGREGATION_DOCUMENT(DocumentFormat.MANUAL),
         DISAGGREGATION_DOCUMENT_CSV(DocumentFormat.CSV),
         DISAGGREGATION_DOCUMENT_XML(DocumentFormat.XML),
         REAGGREGATION_DOCUMENT(DocumentFormat.MANUAL),
         REAGGREGATION_DOCUMENT_CSV(DocumentFormat.CSV),
         REAGGREGATION_DOCUMENT_XML(DocumentFormat.XML),
         LP_INTRODUCE_GOODS(DocumentFormat.MANUAL),
         LP_SHIP_GOODS(DocumentFormat.MANUAL),
         LP_SHIP_GOODS_CSV(DocumentFormat.CSV),
         LP_SHIP_GOODS_XML(DocumentFormat.XML),
         LP_INTRODUCE_GOODS_CSV(DocumentFormat.CSV),
         LP_INTRODUCE_GOODS_XML(DocumentFormat.XML),
         LP_ACCEPT_GOODS(DocumentFormat.MANUAL),
         LP_ACCEPT_GOODS_XML(DocumentFormat.XML),
         LK_REMARK(DocumentFormat.MANUAL),
         LK_REMARK_CSV(DocumentFormat.CSV),
         LK_REMARK_XML(DocumentFormat.XML),
         LK_RECEIPT(DocumentFormat.MANUAL),
         LK_RECEIPT_XML(DocumentFormat.XML),
         LK_RECEIPT_CSV(DocumentFormat.CSV),
         LP_GOODS_IMPORT(DocumentFormat.MANUAL),
         LP_GOODS_IMPORT_CSV(DocumentFormat.CSV);

         private DocumentFormat documentFormat;

         DocumentType(DocumentFormat documentFormat) {
             this.documentFormat = documentFormat;
         }

         public DocumentFormat getDocumentFormat() {
             return documentFormat;
         }
     }
     enum DocumentFormat {
        MANUAL,
        XML,
        CSV;
    }


     abstract class ProductDocument {
         private DocumentType documentType;

         public DocumentType getDocumentType() {
             return documentType;
         }
     }
     class ProductDocumentMadeInRus extends ProductDocument {

        private Description description;
        private String doc_id;
        private String doc_status;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private DateFormat production_date;
        private String production_type;
        private List<ProductMadeInRussia> products;
        private DateFormat reg_date;
        private String reg_number;

         public ProductDocumentMadeInRus() {
         }

         public ProductDocumentMadeInRus(Description description, String doc_status, DocumentType documentType, boolean importRequest, String owner_inn, String participant_inn, String producer_inn, DateFormat production_date, String production_type, List<ProductMadeInRussia> products, DateFormat reg_date, String reg_number) {
             this.description = description;
             this.doc_status = doc_status;
             super.documentType = documentType;
             this.importRequest = importRequest;
             this.owner_inn = owner_inn;
             this.participant_inn = participant_inn;
             this.producer_inn = producer_inn;
             this.production_date = production_date;
             this.production_type = production_type;
             this.products = products;
             this.reg_date = reg_date;
             this.reg_number = reg_number;
         }

         public DocumentType getDocumentType() {
             return super.documentType;
         }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;
             ProductDocumentMadeInRus that = (ProductDocumentMadeInRus) o;
             return importRequest == that.importRequest && Objects.equals(description, that.description) && Objects.equals(doc_status, that.doc_status) && Objects.equals(owner_inn, that.owner_inn) && Objects.equals(participant_inn, that.participant_inn) && Objects.equals(producer_inn, that.producer_inn) && Objects.equals(production_date, that.production_date) && Objects.equals(production_type, that.production_type) && Objects.equals(products, that.products) && Objects.equals(reg_date, that.reg_date) && Objects.equals(reg_number, that.reg_number);
         }

         @Override
         public int hashCode() {
             return Objects.hash(description, doc_status, importRequest, owner_inn, participant_inn, producer_inn, production_date, production_type, products, reg_date, reg_number);
         }

         @Override
         public String toString() {
             return "ProductDocumentMadeInRus{" +
                     "documentType=" + super.documentType +
                     ", description=" + description +
                     ", doc_id='" + doc_id + '\'' +
                     ", doc_status='" + doc_status + '\'' +
                     ", importRequest=" + importRequest +
                     ", owner_inn='" + owner_inn + '\'' +
                     ", participant_inn='" + participant_inn + '\'' +
                     ", producer_inn='" + producer_inn + '\'' +
                     ", production_date=" + production_date +
                     ", production_type='" + production_type + '\'' +
                     ", products=" + products +
                     ", reg_date=" + reg_date +
                     ", reg_number='" + reg_number + '\'' +
                     '}';
         }
     }
    class Description {
        private String participantInn;

        public String getParticipantInn() {
            return participantInn;
        }

        public Description(String participantInn) {
            this.participantInn = participantInn;
        }

        @Override
        public String toString() {
            return "Description{" +
                    "participantInn='" + participantInn + '\'' +
                    '}';
        }
    }


     abstract class Product{

     }
     class ProductMadeInRussia extends Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private DateFormat production_date;
        private String tnved_code;
        private String uid_code;
        private String uitu_code;

         public ProductMadeInRussia() {
         }
         public ProductMadeInRussia(String certificate_document, String certificate_document_date, String certificate_document_number, String owner_inn, String producer_inn, DateFormat production_date, String tnved_code, String uid_code, String uitu_code) {
             this.certificate_document = certificate_document;
             this.certificate_document_date = certificate_document_date;
             this.certificate_document_number = certificate_document_number;
             this.owner_inn = owner_inn;
             this.producer_inn = producer_inn;
             this.production_date = production_date;
             this.tnved_code = tnved_code;
             this.uid_code = uid_code;
             this.uitu_code = uitu_code;
         }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;
             ProductMadeInRussia that = (ProductMadeInRussia) o;
             return Objects.equals(certificate_document, that.certificate_document) && Objects.equals(certificate_document_date, that.certificate_document_date) && Objects.equals(certificate_document_number, that.certificate_document_number) && Objects.equals(owner_inn, that.owner_inn) && Objects.equals(producer_inn, that.producer_inn) && Objects.equals(production_date, that.production_date) && Objects.equals(tnved_code, that.tnved_code) && Objects.equals(uid_code, that.uid_code) && Objects.equals(uitu_code, that.uitu_code);
         }
         @Override
         public int hashCode() {
             return Objects.hash(certificate_document, certificate_document_date, certificate_document_number, owner_inn, producer_inn, production_date, tnved_code, uid_code, uitu_code);
         }
         @Override
         public String toString() {
             return "ProductMadeInRussia{" +
                     "certificate_document='" + certificate_document + '\'' +
                     ", certificate_document_date='" + certificate_document_date + '\'' +
                     ", certificate_document_number='" + certificate_document_number + '\'' +
                     ", owner_inn='" + owner_inn + '\'' +
                     ", producer_inn='" + producer_inn + '\'' +
                     ", production_date=" + production_date +
                     ", tnved_code='" + tnved_code + '\'' +
                     ", uid_code='" + uid_code + '\'' +
                     ", uitu_code='" + uitu_code + '\'' +
                     '}';
         }
     }




}


