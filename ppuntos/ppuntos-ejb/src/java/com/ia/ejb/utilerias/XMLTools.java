/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.ejb.utilerias;

/**
 *
 * @author Kryzpy
 */

 

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.UnsupportedEncodingException;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.Connection;

import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;

import java.util.List;

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.sql.DataSource;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

 

/**

* <p>Title: SadasEJB</p>

* <p>Description: EJB del sistema Sadas</p>

* <p>Copyright: Copyright (c) 2003</p>

* <p>Company: CFE</p>

* @author not attributable

* @version 1.0

*/

 

public class XMLTools {

   

    static boolean modoDePruebas = true;

  // Constructor de default vacio

  public XMLTools() {

  }

 

public static String xmlQueryBase(String sSql, String rootname, String rowname) {

    try {

      InitialContext jndiContext = new InitialContext( );

      DataSource ds = (DataSource) jndiContext.lookup("java:/jboss/datasources/pelipuntos");

     

      DocumentBuilderFactory dbf;

      DocumentBuilder db;

      Document md;

 

      dbf = DocumentBuilderFactory.newInstance();

      dbf.setCoalescing(true);

      dbf.setExpandEntityReferences(true);

      dbf.setIgnoringComments(true);

      db = dbf.newDocumentBuilder();

      Connection con = ds.getConnection();

      Statement st = con.createStatement();

  

      md = db.newDocument();

      if(modoDePruebas)

        System.out.println(sSql);

      Node root_node = XMLTools.creaRaizXML(md, st.executeQuery(sSql), rootname, rowname);

      md.appendChild(root_node);

     

      st.close();

      con.close();

      return XMLTools.docToString(md);     

    }

    catch(NamingException ne) {

    //  System.out.println(ne.getMessage());

      throw new RuntimeException("Ocurrio un error al buscar el nombre del contexto del datasource", ne);

    }

    catch(ParserConfigurationException pe) {

     // System.out.println("Ocurrio un error al crear los objetos XML");

      throw new RuntimeException("Ocurrio un error al crear los objetos XML", pe);

    }

    catch(SQLException se) {

    //  System.out.println("Ocurrio un error de JDBC");

      throw new RuntimeException("Ocurrio un error de JDBC", se);

    }

  } // xmlQuery

 

 

//----------------------------------------------------------------------------

  /**

   *   Crea un nodo DOM con todos los elementos de un ResultSet, si los datos

   *   SQL son NULOS, una cadena vacia es insertada.

   *   Node XML format:  <sqldata>

   *                       <dataelement_rowindex>

   *                          <dataelement_colindex>

   *                            the_sql_column_data

   *                          </dataelement_colindex>

   *                          ...

   *                       </dataelement_rowindex>

   *                       ...

   *                     </sqldata>

   *

   *   @throws SQLException

   *   @param document Documento DOM donde se almacenaran los datos

   *   @param result ResultSet de donde se obtendra el XML

   *   @param rsname Nombre del ResultSet que se aplicara al nodo raiz

   *   @param rowname Nombre de cada child que representa una fila

   *   @return Un nodo DOM raiz

   */

//----------------------------------------------------------------------------

  public static Node creaRaizXML(Document document,

                                 ResultSet result, String rsname, String rowname) throws SQLException {

    Element root_element;

    Element row_element;

    Element column_element;

    String result_string;

    int col_count;

    int max_columns, asciiVal;

    StringBuffer sb = new StringBuffer();

 

    ResultSetMetaData rm = result.getMetaData();

    max_columns=rm.getColumnCount();

    root_element=document.createElement(rsname);

    while (result.next()) {

      row_element=document.createElement(rowname);

 

      for (col_count=1; col_count <= max_columns; col_count++) {

        column_element=document.createElement(rm.getColumnLabel(col_count).toLowerCase());

        result_string=result.getString(col_count);

        if (result_string == null) {

          result_string="";

        }

        else {

          /*

          for(int k=0; k<result_string.length(); k++) {

            if (result_string.charAt(k)=='Ñ')

              System.out.println("A fuerzas hay una Ñ");

            if (result_string.charAt(k) > 0x80) {

              sb.append("&#");

              asciiVal = result_string.charAt(k);

              sb.append(asciiVal);

              sb.append(';');

              System.out.println(sb.toString());

            }

            else {

              sb.append(result_string.charAt(k));

            }

          }

          */

        }

        column_element.appendChild(document.createTextNode(result_string));

        row_element.appendChild(column_element);

      }

      root_element.appendChild(row_element);

    }

    return root_element;

  }

 

  /**

   * docToString convierte un Document XML a su representación en cadena

   * @param doc DOM Document que contiene el XML

   * @return Cadena XML

   */

  public static String docToString(Document doc) {

          try {

              // Prepare the DOM document for writing

              Source source = new DOMSource(doc);

 

              // Prepare the output file

              ByteArrayOutputStream bs = new ByteArrayOutputStream();

              Result result = new StreamResult(bs);

              // Write the DOM document to the file

              Transformer xformer = TransformerFactory.newInstance().newTransformer();

              xformer.setOutputProperty(OutputKeys.ENCODING, "iso8859-1");

              xformer.transform(source, result);

             

              return bs.toString(xformer.getOutputProperty(OutputKeys.ENCODING));

          } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("UnsupportedEncodingException en docToString", e);

          }         

          catch (TransformerConfigurationException e) {

            throw new RuntimeException("TransformerConfigurationException en docToString", e);

          } catch (TransformerException e) {

            throw new RuntimeException("TransformerException en docToString", e);

          }

      }

 

    public List<String[]> parseXMLtoListString(String xml, String nombreHijo) {

        List collection = null;

        try {

            collection = new ArrayList();

 

            InputStream is = new ByteArrayInputStream(xml.getBytes());

            //System.out.println("Se  crea el inpues stream");

            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            org.w3c.dom.Document document = parser.parse(is);

 

            NodeList nl = document.getElementsByTagName(nombreHijo);

            int cont = nl.getLength();

            for (int i = 0; i < cont; i++) {

                Node node = nl.item(i);

                NodeList hijo = node.getChildNodes();

                String salida = "";

                // LabelValueBean label = new LabelValueBean("","");

                String s[] = new String[hijo.getLength()];

                for (int j = 0; j < hijo.getLength(); j++) {

                    Node node1 = hijo.item(j);

                    NodeList hijo1 = node1.getChildNodes();

                    if (hijo1.item(0) != null) {

                        if (j == 0) {

                           // System.out.println("Crea el valor del label");

                            //  label.setValue(hijo1.item(0).getTextContent());

                            s[j] = hijo1.item(0).getTextContent();

                        } else {

                            // System.out.println("Crea el caption del label");

                            // label.setLabel( label.getLabel() + " " + hijo1.item(0).getTextContent());

                            s[j] = hijo1.item(0).getTextContent();

                        }

                    } else {

                        //label.setLabel( label.getLabel() + " " + hijo1.item(0).getTextContent());

                        //salida += "|" ;

                        s[j] = "";

                    }

                }

                collection.add(s);

                //System.out.print(salida);

            }

 

        } catch (SAXException ex) {

        } catch (IOException ex) {

        } catch (ParserConfigurationException e) {

            // TODO

        }

        return collection;

    } 

} // class XMLUtils