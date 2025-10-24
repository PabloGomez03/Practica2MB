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
        boolean end = false, start = true, done = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = "";
            String id = "";
            ArrayList<String> words = new ArrayList();
            int it = 0;

            final SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();

            do {

                words.clear();

                if (start) {
                    line = br.readLine();
                }

                if (line == null || line.trim().isEmpty()) {

                    end = true;

                } else {

                    id = line.substring(2).trim();
                    br.readLine();

                    line = br.readLine();
                    done = false;
                    while (line != null && !line.startsWith(".I")) {

                        String trimmedLine = line.trim();

                        if (!trimmedLine.isEmpty()) {
                            String[] ar = trimmedLine.split("\\s+");

                            for (int i = 0; i < ar.length; i++) {
                                if (words.size() < 5) {
                                    words.add(ar[i]);
                                }
                            }
                        }

                        line = br.readLine();
                    }

                    if (line == null) {
                        end = true;
                    }

                    System.out.println("\nPalabras de id ->" + id + "\n-----------------------");
                    for (String w : words) {
                        System.out.println("| " + w);
                    }

                    String searchTerms = String.join(" ", words);
                    String escapedSearchTerms = ClientUtils.escapeQueryChars(searchTerms);
                    SolrQuery q = new SolrQuery();

                    q.setQuery(escapedSearchTerms);

                    q.set("df", "text");
                    q.setRows(100);

                    QueryResponse response = client.query("Practica1", q);

                    SolrDocumentList list = response.getResults();

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Queries\\queryRes" + (it + 1)))) {
                        for (SolrDocument doc : list) {
                            bw.write("Document " + doc.getFieldValue("identifier") + "\n------------------\n");
                            bw.write(doc.getFieldValue("text") + "\n\n");
                        }
                    }

                }

                id = "";
                it++;
                start = false;

            } while (!end);
            client.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
