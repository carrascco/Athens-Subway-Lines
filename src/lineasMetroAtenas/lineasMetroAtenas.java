package lineasMetroAtenas;

import es.upm.aedlib.Pair;
import es.upm.aedlib.graph.*;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class lineasMetroAtenas {

  public static ArrayList<Estacion> lista;

  public static Map<String, Pair<Double, Double>> coords = new HashTableMap<String, Pair<Double, Double>>();

  public static DirectedGraph<Estacion, Integer> g = new DirectedAdjacencyListGraph<Estacion, Integer>();

  /*
   *
   * Esta funcion lee de un fichero donde estan las estaciones, un valor inicial
   * para la h que será 0
   * y sus cordenadas para la interfaz
   *
   * @param
   *
   * @return de vuelve una lista de Estaciones
   */
  public static ArrayList<Estacion> listaEstaciones()
      throws FileNotFoundException, IOException {
    ArrayList<Estacion> lista = new ArrayList<>();
    String linea;
    FileReader f = new FileReader(
        new File("src/lineasMetroAtenas/Estaciones.txt"));
    BufferedReader b = new BufferedReader(f);
    while ((linea = b.readLine()) != null) {
      String[] atributos = linea.split(";");
      String nombre = atributos[0];
      lista.add(
          new Estacion(
              nombre,
              Integer.parseInt(atributos[1]),
              0,
              0,
              Integer.parseInt(atributos[2]),
              Integer.parseInt(atributos[3])));
    }
    b.close();
    return lista;
  }

  public static void establecerMapaLocal()
      throws FileNotFoundException, IOException {
    String linea;
    FileReader f = new FileReader(
        new File("src/lineasMetroAtenas/Estaciones.txt"));
    BufferedReader b = new BufferedReader(f);
    while ((linea = b.readLine()) != null) {
      String[] atributos = linea.split(";");
      String nombre = atributos[0];
      Pair<Double, Double> pair = new Pair<>(
          Double.parseDouble(atributos[4]),
          Double.parseDouble(atributos[5]));
      coords.put(nombre, pair);
    }
    b.close();
  }

  /*
   *
   * Esta funcion ordena alfabeticamente las estaciones
   *
   * @param Array String
   *
   * @return
   */
  public static void ordenarEstacionesAlfabeticamente(String lista[]) {
    for (int i = 0; i < (lista.length - 1); i++) {
      for (int j = i + 1; j < lista.length; j++) {
        if (lista[i].compareToIgnoreCase(lista[j]) > 0) {
          String aux = lista[i];
          lista[i] = lista[j];
          lista[j] = aux;
        }
      }
    }
  }

  /*
   *
   * Esta funcion se queda solo con el nombre de cada estacion
   *
   * @param Array Estacion
   *
   * @return Array String
   */
  public static String[] nombreEstaciones(ArrayList<Estacion> listaEstaciones1) {
    String[] estaciones = new String[58];
    for (int i = 0; i < listaEstaciones1.size(); i++) {
      estaciones[i] = listaEstaciones1.get(i).getEstacion();
    }
    ordenarEstacionesAlfabeticamente(estaciones);
    return estaciones;
  }

  /*
   *
   * Esta funcion busca una estacion concreta
   *
   * @param String
   *
   * @return Estacion
   */
  public static Estacion buscarEstacion(String nombre) throws FileNotFoundException, IOException {
    Estacion encontrada = null;
    boolean esta = false;
    Iterator<Estacion> it = lista.iterator();
    while (!esta && it.hasNext()) {
      Estacion est = it.next();
      String a = est.getEstacion();
      if (a.equals(nombre)) {
        encontrada = est;
        esta = true;
      }
    }
    return encontrada;
  }

  public static Recorrido ProcesaPeticion(
      String EstacionOrigen,
      String EstacionDestino) throws FileNotFoundException, IOException {
    String estacionO = EstacionOrigen;
    String estacionD = EstacionDestino;

    int nTrasbordos = -1;
    if (EstacionOrigen.equals(EstacionDestino)) {
      Recorrido res = new Recorrido();
      res.recorridoEstaciones[0] = EstacionOrigen;
      nTrasbordos = 0;
      return res;
    } else {
      establecerMapaLocal();
      crearGrafo();
      algoritmoAEstrella(buscarEstacion(estacionO), buscarEstacion(estacionD));

      Estacion Destino = buscarEstacion(estacionD);
      List<Estacion> rutaOptima = new ArrayList<Estacion>();

      for (Estacion nodo = Destino; nodo != null; nodo = nodo.getParent()) {
        rutaOptima.add(nodo);
      }
      Collections.reverse(rutaOptima);

      String recorrido = "";
      int cont = 0;

      Recorrido res = new Recorrido();

      for (int i = 0; i < rutaOptima.size(); i++) {
        cont++;
        Estacion estacionAux = rutaOptima.get(i);
        res.recorridoEstaciones[cont - 1] = estacionAux.getEstacion();

        if (i != rutaOptima.size() - 1) {
          recorrido = recorrido + estacionAux + " > ";
        } else {
          recorrido = recorrido + estacionAux + "\n";
        }
        
          if (i != rutaOptima.size() - 1) {
            if (rutaOptima.get(i ).getLinea() != rutaOptima.get(i + 1).getLinea()){
              if(nTrasbordos==-1) nTrasbordos=1;
              else nTrasbordos++;
          }}
        
      }
      rutaOptima = null;

      if (nTrasbordos < 0)
        nTrasbordos = 0;
      res.trasbordos = (int) nTrasbordos;
      resetParents();
      return res;
    }
  }
  public static void resetParents(){
    Iterator<Vertex<Estacion>> it=g.vertices().iterator();
    while(it.hasNext()){
      Vertex<Estacion> v=it.next();
      Estacion est=v.element();
      est.setPadre(null);
      est.setfMin(0);
      est.setgMin(0);
      est.sethMin(0);
    }
  }

  private static double distanciaCoord(Estacion est1, Estacion est2) {
    double radioTierra = 6371; // en kil�metros
    double dLat = Math.toRadians(
        coords.get(est2.getEstacion()).getLeft() -
            coords.get(est1.getEstacion()).getLeft());
    double dLng = Math.toRadians(
        coords.get(est2.getEstacion()).getRight() -
            coords.get(est1.getEstacion()).getRight());
    double sindLat = Math.sin(dLat / 2);
    double sindLng = Math.sin(dLng / 2);
    double va1 = Math.pow(sindLat, 2) +
        Math.pow(sindLng, 2) *
            Math.cos(Math.toRadians(coords.get(est1.getEstacion()).getLeft())) *
            Math.cos(Math.toRadians(coords.get(est2.getEstacion()).getLeft()));
    double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
    double distancia = radioTierra * va2;

    return distancia;
  }

  public static void algoritmoAEstrella(
      Estacion EstacionInicio,
      Estacion EstacionDestino) {
    ArrayList<Estacion> abiertos = new ArrayList<Estacion>();
    ArrayList<Estacion> cerrados = new ArrayList<Estacion>();
    EstacionInicio.inicializarNodo();
    EstacionInicio.sethMin(distanciaCoord(EstacionInicio, EstacionDestino));
    abiertos.add(EstacionInicio);
    Estacion aux = EstacionInicio;
    boolean enc = false;
    double fn, gn, hn;
    int aux1=1;
    while (abiertos != null && !enc) {
      if (aux.getEstacion().equals(EstacionDestino.getEstacion())) {
        enc = true; 
      }
      double aux2 = 1000000000;
      for (int i = 0; i < abiertos.size() && aux1 == 1; i++) {
        aux2 = abiertos.get(i).getF();
        aux = abiertos.get(i);
      }
      for (int i = 0; i < abiertos.size() && aux1 == 2; i++) {
        if (abiertos.get(i).getF() < aux2) {
          aux2 = abiertos.get(i).getF();
          aux = abiertos.get(i);
        }
      }
      
      aux1 = 2;

      cerrados.add(aux);
      abiertos.remove(aux);
      Vertex<Estacion> auxEst = get(g.vertices(), aux.getEstacion());
      for (Edge<Integer> arista : g.outgoingEdges(auxEst)) {
        Estacion hijo = g.endVertex(arista).element();
        hn = distanciaCoord(hijo, EstacionDestino);
        gn = auxEst.element().getG() + arista.element();
        fn = hn + gn;
        if (!abiertos.contains(hijo) && !cerrados.contains(hijo)) {
          abiertos.add(hijo);
          hijo.setfMin(fn);
          hijo.setgMin(gn);
          hijo.sethMin(hn);
          hijo.setPadre(aux);
        } else if (abiertos.contains(hijo) && hijo.getF() > fn) {
          hijo.setPadre(aux);
          hijo.setfMin(fn);
          hijo.setgMin(gn);
        }
      }
    }
  }

  // Funcion auxiliar que devuelve el vertice al recibir la lista de vertices y el
  // valor que se busca
  public static Vertex<Estacion> get(
      Iterable<Vertex<Estacion>> vertices,
      String element) {
    Iterator<Vertex<Estacion>> it = vertices.iterator();
    while (it.hasNext()) {
      Vertex<Estacion> actual = (Vertex<Estacion>) it.next();
      if (actual.element().getEstacion().equals(element)) {
        return actual;
      }
    }
    return null;
  }

  public static void insertarVertices(ArrayList<Estacion> estaciones) {
    for (int i = 0; i < estaciones.size(); i++) {
      g.insertVertex(estaciones.get(i));
    }
  }

  public static DirectedGraph<Estacion, Integer> crearGrafo()
      throws FileNotFoundException, IOException {
    FileReader f = new FileReader(
        new File("src/lineasMetroAtenas/Adyacencias.txt"));
    BufferedReader b = new BufferedReader(f);
    String linea;
    insertarVertices(lista);
    while ((linea = b.readLine()) != null) {
      String[] data = linea.split(";");
      String adyacente1 = data[0];
      String adyacente2 = data[1];
      int distanciaReal = Integer.parseInt(data[2]);
      g.insertDirectedEdge(
          get(g.vertices(), adyacente1),
          get(g.vertices(), adyacente2),
          distanciaReal);
      g.insertDirectedEdge(
          get(g.vertices(), adyacente2),
          get(g.vertices(), adyacente1),
          distanciaReal);
      // System.out.println(g.edges().toString());
    }
    b.close();
    return g;
  }

  public static void main(String[] args) throws IOException {
    lista = listaEstaciones();
    Interfaz pantalla = new Interfaz();
    pantalla.setVisible(true);

  }
}
