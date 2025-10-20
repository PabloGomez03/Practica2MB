/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package practicasolr.practica2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.SolrQuery;

/**
 *
 * @author apolo
 */
public class Practica2 {

    public static void main(String[] args) {

        String file = "..\\corpus\\MED.QRY";
        boolean end = false, start = true, done = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = "";
            String id = "";

            final SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();

            do {
                ArrayList<String> words = new ArrayList();
                
                if (start) {
                    line = br.readLine();
                }

                if (line.length() == 0) {

                    end = true;

                } else {

                    id = line.trim().split("I")[1];
                    br.readLine();

                    line = br.readLine();
                    done = false;
                    while (line != null && line.charAt(1) != 'I') {

                        String[] ar = line.split("\\s+");

                        for (int i = 0; i < ar.length; i++) {
                            if (words.size() < 6) {
                                words.add(ar[i]);
                            }

                        }

                        line = br.readLine();
                    }
                    

                    if (line == null) {
                        end = true;
                    }

                }
                
                
                System.out.println("\nPalabras de id ->"+id+"\n-----------------------");
                for(String w:words){
                    System.out.println("| "+w);
                    
                    SolrQuery q = new SolrQuery();
                    
                    q.setQuery("");
                    
                    
                }
                
                
                //System.out.println(id+" -------> "+text+"\n");
                /*SolrInputDocument doc = new SolrInputDocument();
                doc.addField("identifier", id);
                doc.addField("text", text);
                UpdateResponse updateResponse = client.add("Practica1", doc);
                client.commit("Practica1");

                
                id = "";
                text = "";*/
                id = "";
                
                start = false;
                
            } while (!end);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    }

}

