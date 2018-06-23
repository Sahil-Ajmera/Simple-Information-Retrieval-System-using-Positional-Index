/*
Author : Sahil Ajmera
Lab 2 using Postitional Index
 */
//package lab2;

import java.util.ArrayList;
import java.io.*;
import java.util.*;


public class PositionalIndex {
    String[] myDocs;
    ArrayList<String> termList;
    ArrayList<ArrayList<DocId>> docLists;
    int distance = 1;

    /**
     * Construct a positional index
     * @param docs List of input strings or file names
     *
     */
    public PositionalIndex(String[] docs)
    {
        //TASK1: TO BE COMPLETED
        System.out.println("****************************************Task-1****************************************");
        // Taking all string into one.

        termList = new ArrayList<String>();
        docLists = new ArrayList<ArrayList<DocId>>();

        try
        {

            for(int i = 0 ; i< docs.length;i++)
            {
                String all_lines = "";
                File docFile = new File("Lab1_Data" + '/' + docs[i]);
                BufferedReader reader = new BufferedReader(new FileReader(docFile));
                String line = null;
                while((line = reader.readLine()) != null)
                {

                    // Normalization - Take all lower case  .
                    all_lines +=  line.toLowerCase();
                }

                // Tokenization
                String[] tokens  = all_lines.split("[ .,?!:;$%&*+()%#!/\\-\\^\"]+");

                // Porter's stemmer


                for(int j =0 ; j<tokens.length;j++)
                {
                    if(!termList.contains(tokens[j]))
                    {
                        termList.add(tokens[j]);
                        DocId doid = new DocId(i,j);
                        ArrayList<DocId> docList = new ArrayList<DocId>();
                        docList.add(doid);
                        docLists.add(docList);
                    }
                    else
                    {
                        int index = termList.indexOf(tokens[j]);
                        ArrayList<DocId> docList = docLists.get(index);
                        boolean match = false;
                        int k = 0;

                        //old term same document also seen before
                        for(DocId doid:docList)
                        {
                            if(doid.docId == i)
                            {
                                doid.insertPosition(j);
                                match = true;
                                //docList.set(k,doid);
                            }
                            k++;
                        }

                        //old term new document
                        if(!match)
                        {
                            DocId doid = new DocId(i,j);
                            docLists.get(index).add(doid);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            for(int i = 0 ;i<docs.length;i++)
            {
                String all_lines = "";
                all_lines += docs[i].toLowerCase();

                // Tokenization
                String[] tokens  = all_lines.split("[ .,?!:;$%&*+()%#!/\\-\\^\"]+");

                for(int k = 0;k<tokens.length;k++)
                {
                    perform_stemming(tokens[k],tokens,k);
                }

                for(int j =0 ; j<tokens.length;j++)
                {
                    if(!termList.contains(tokens[j]))
                    {
                        termList.add(tokens[j]);
                        DocId doid = new DocId(i,j);
                        ArrayList<DocId> docList = new ArrayList<DocId>();
                        docList.add(doid);
                        docLists.add(docList);
                    }
                    else
                    {
                        int index = termList.indexOf(tokens[j]);
                        ArrayList<DocId> docList = docLists.get(index);
                        boolean match = false;
                        int k = 0;

                        //old term same document also seen before
                        for(DocId doid:docList)
                        {
                            if(doid.docId == i)
                            {
                                doid.insertPosition(j);
                                match = true;
                                //docList.set(k,doid);
                            }
                            k++;
                        }

                        //old term new document
                        if(!match)
                        {
                            DocId doid = new DocId(i,j);
                            docLists.get(index).add(doid);
                        }
                    }
                }
            }
        }

    }

    public void perform_stemming(String token,String[] tokens,int index)
    {
        Stemmer st = new Stemmer();
        st.add(token.toCharArray(),token.length());
        st.stem();
        tokens[index] = st.toString();
    }

    /**
     * Return the string representation of a positional index
     */
    public String toString()
    {
        String matrixString = new String();
        ArrayList<DocId> docList;
        for(int i=0;i<termList.size();i++){
            matrixString += String.format("%-15s", termList.get(i));
            docList = docLists.get(i);
            for(int j=0;j<docList.size();j++)
            {
                matrixString += docList.get(j)+ "\t";
            }
            matrixString += "\n";
        }
        return matrixString;
    }

    /**
     *
     * @param l1 first postings
     * @param l2 second postings
     * @return merged result of two postings
     */
    public ArrayList<DocId> intersect(ArrayList<DocId> l1, ArrayList<DocId> l2)
    {
        //TASK2: TO BE COMPLETED
        if(l1 == null)
            return l2;
        else if (l2 == null)
            return l1;
        ArrayList<DocId> mergedList = new ArrayList<DocId>();
        int id1 = 0 ,id2 = 0;
        while(id1 < l1.size() && id2 < l2.size())
        {
            if(l1.get(id1).docId == l2.get(id2).docId)
            {
                ArrayList<Integer> pp1 = l1.get(id1).positionList;
                ArrayList<Integer> pp2 = l2.get(id2).positionList;
                int pid1 = 0,pid2 = 0;
                boolean match = false;
                while(pid1 < pp1.size())
                {
                    pid2 = 0;
                    while(pid2 < pp2.size())
                    {
                        if(pp2.get(pid2) - pp1.get(pid1) ==  distance)
                        {
                            if(!match)
                            {
                                // If matching document has not been added originally
                                if(pp1.get(pid1) > pp2.get(pid2))
                                {
                                    DocId docList = new DocId(l1.get(id1).docId, pp2.get(pid2));
                                    mergedList.add(docList);
                                }
                                else
                                {
                                    // matching document has been added originally
                                    DocId docList = new DocId(l1.get(id1).docId, pp1.get(pid1));
                                    mergedList.add(docList);
                                }
                                match = true;
                            }
                            else
                            {
                                int k = 0;
                                for(DocId docList:mergedList)
                                {
                                    if(l1.get(id1).docId == docList.docId)
                                    {
                                        if(pid1 > pid2) {
                                            mergedList.get(k).insertPosition(pid2);
                                        }
                                        else
                                        {
                                            mergedList.get(k).insertPosition(pid1);
                                        }
                                    }
                                    k++;
                                }
                            }
                        }
                        //else if(pp2.get(pid2) > pp1.get(pid1))
                          //      break;
                        pid2++;
                    }
                    pid1++;
                }
                id1++;
                id2++;
            }
            else if(l1.get(id1).docId > l2.get(id2).docId)
                id2++;
            else
                id1++;
        }
        return mergedList;
    }

    /**
     *
     * @param query a phrase query that consists of any number of terms in the sequential order
     * @return ids of documents that contain the phrase
     */
    public ArrayList<DocId> phraseQuery(String[] query)
    {
        distance = 1;
        ArrayList<DocId> docList1 = new ArrayList<DocId>();
        ArrayList<DocId> docList2 = new ArrayList<DocId>();
        ArrayList<DocId> docList = new ArrayList<DocId>();
        //ArrayList<ArrayList<DocId>> result = new ArrayList<ArrayList<DocId>>();

        if(query.length == 0)
            return null;
        else if(query.length == 1)
        {
            docList1 = docLists.get(termList.indexOf(query[0]));
            return docList1;
        }
        else {
            ArrayList<DocId> result = new ArrayList<DocId>();
            if (termList.contains(query[0]))
                docList1 = docLists.get(termList.indexOf(query[0]));
            else
                docList1 = null;
            if (termList.contains(query[0]))
                docList2 = docLists.get(termList.indexOf(query[1]));
            else
                docList2 = null;

            result = intersect(docList1, docList2);
            distance++;
            for (int i = 2; i < query.length; i++) {
                if (termList.contains(query[i]))
                    docList = docLists.get(termList.indexOf(query[i]));
                else
                    docList = null;
                result = intersect(result, docList);
                distance++;
            }
            return result;
        }

        //TASK3: TO BE COMPLETED
    }



    public static void main(String[] args)
    {
        String[] docs = {"new home sales top forecasts",
                "home sales rise in july",
                "increase in home sales in july",
                "july new home sales rise"
        };
        Scanner scanner = new Scanner(System.in);
        //place files in Lab1_Data folder if using files
        //String[] docs = {"cv000_29416.txt","cv001_19502.txt","cv002_17424.txt","cv003_12683.txt","cv004_12641.txt"};
        PositionalIndex pi = new PositionalIndex(docs);
        System.out.print(pi);
        System.out.println("****************************************Task-2****************************************");
        System.out.println("Eg.Checking for 2nd and 5th list");
        ArrayList<DocId> result = pi.intersect(pi.docLists.get(2),pi.docLists.get(5));
        if(result == null)
        {
            System.out.println("<>");
        }
        else {
            System.out.println(result);
        }
        System.out.println("****************************************Task-3****************************************");
        System.out.println("Enter a phrase query of 2 words");
        String phraseQuery = scanner.nextLine();
        result = pi.phraseQuery(phraseQuery.split(" "));
        if(result == null)
        {
            System.out.println("Not found");
        }
        else
        {
            System.out.println("Found :"+result);
        }
        //TASK4: TO BE COMPLETED: design and test phrase queries with 2-5 terms
        System.out.println("****************************************Task-4****************************************");
        System.out.println("Enter a phrase query of 2-5 words");
        phraseQuery = scanner.nextLine();
        String[] tokens = phraseQuery.split("[ .,?!:;$%&*+()%#!/\\-\\^\"]+");
        for(int k = 0;k<tokens.length;k++)
        {
            pi.perform_stemming(tokens[k],tokens,k);
        }
        result = pi.phraseQuery(tokens);
        if(result == null)
        {
            System.out.println("Not found");
        }
        else
        {
            System.out.println("Found :"+result);
        }

    }
}

/**
 *
 * @author qyuvks
 * Document id class that contains the document id and the position list
 */
class DocId{
    int docId;
    ArrayList<Integer> positionList;
    public DocId(int did)
    {
        docId = did;
        positionList = new ArrayList<Integer>();
    }
    public DocId(int did, int position)
    {
        docId = did;
        positionList = new ArrayList<Integer>();
        positionList.add(new Integer(position));
    }

    public void insertPosition(int position)
    {
        positionList.add(new Integer(position));
    }

    public String toString()
    {
        String docIdString = ""+docId + ":<";
        for(Integer pos:positionList)
            docIdString += pos + ",";
        docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
        return docIdString;
    }
}
