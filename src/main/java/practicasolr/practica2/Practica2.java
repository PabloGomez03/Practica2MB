package practicasolr.practica2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Practica2 {

    public static void main(String[] args) throws SolrServerException, IOException {

        String file = "..\\corpus\\MED.QRY";
        boolean end = false, start = true;

        try (BufferedReader br = new BufferedReader(new FileReader(file)); BufferedWriter bw = new BufferedWriter(new FileWriter("Prueba\\trec_top_file"))) {

            String line = "";
            String id = "";
            String text = "";

            final SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();

            do {

                if (start) {
                    line = br.readLine();
                }

                if (line == null || line.trim().isEmpty()) {

                    end = true;

                } else {

                    id = line.substring(2).trim();
                    br.readLine();

                    line = br.readLine();
                    while (line != null && !line.startsWith(".I")) {

                        text += line;

                        line = br.readLine();
                    }

                    if (line == null) {
                        end = true;
                    }

                    String escapedSearchTerms = ClientUtils.escapeQueryChars(text);
                    SolrQuery q = new SolrQuery();
                    q.setQuery(escapedSearchTerms);
                    q.setFields("score");
                    q.setFields("identifier");
                    q.set("df", "text");
                    q.set("fl", "*, score");

                    q.setRows(100);
                    QueryResponse response = client.query("Practica1", q);

                    SolrDocumentList list = response.getResults();

                    int j = 0;
                    for (SolrDocument doc : list) {

                        Float score = (Float) doc.getFieldValue("score");

                        float scoreValue = 0.0f;
                        if (score != null) {
                            scoreValue = score.floatValue();
                        }

                        bw.write(id + " P0 " + doc.getFieldValue("identifier") + " " + (j++) + " " + scoreValue + " MB\n");
                    }

                }

                id = "";
                text = "";

                start = false;

            } while (!end);

            client.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
