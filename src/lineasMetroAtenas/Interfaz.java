package lineasMetroAtenas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Interfaz extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton botonAccion;
	private ImageIcon mapa = new ImageIcon(getClass().getResource("/Imagenes/MapaAtenas.jpg"));
	private ImageIcon puntoSalida = new ImageIcon(getClass().getResource("/Imagenes/dotIniFin.png"));
	private ImageIcon puntoDestino = new ImageIcon(getClass().getResource("/Imagenes/dotIniFin.png"));
	private ImageIcon lineas = new ImageIcon(getClass().getResource("/Imagenes/dotCamino.png"));
	private JLabel planoFondo;

	private javax.swing.JComboBox<String> jComboBoxSalida;
	private javax.swing.JComboBox<String> jComboBoxDestino;


	private String[] listaEstaciones = new String[58];

	JTextField numeroTransbordos;
	JTextPane recorrido;
	JScrollPane scrollpanel;
	JPanel panel;

	public Interfaz() {
		// Caracter�sticas del frame
		setTitle("lineasMetroAtenas");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 680);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		// Caracter�sticas del panel
		panel = (JPanel) this.getContentPane();
		panel.setLayout(null);

		// Plano en panel
		planoFondo = new JLabel();
		planoFondo.setIcon(mapa);
		planoFondo.setBounds(0, -180, 1900, 1000);
		panel.add(planoFondo);
		planoFondo.setOpaque(false);

		// Literal de estaci�n origen
		JLabel label1 = new JLabel("Estacion origen:");
		panel.add(label1);
		Dimension size1 = label1.getPreferredSize();
		label1.setBounds(630, 10, size1.width, size1.height);

		// Lista para elegir estaci�n origen
		try {
			listaEstaciones = lineasMetroAtenas.nombreEstaciones(lineasMetroAtenas.lista);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		jComboBoxSalida = new javax.swing.JComboBox<>();
		jComboBoxSalida.setBounds(630, 30, 150, 30);
		jComboBoxSalida.setModel(new javax.swing.DefaultComboBoxModel<>(listaEstaciones));
		panel.add(jComboBoxSalida);
		// jComboBoxOrigen.addActionListener(this);

		// Literal de estaci�n destino
		JLabel label2 = new JLabel("Estacion destino:");
		panel.add(label2);
		Dimension size2 = label2.getPreferredSize();
		label2.setBounds(630, 70, size2.width, size2.height);
		label2.setOpaque(true);

		// Lista para elegir estaci�n destino
		jComboBoxDestino = new javax.swing.JComboBox<>();
		jComboBoxDestino.setBounds(630, 90, 150, 30);
		jComboBoxDestino.setModel(new javax.swing.DefaultComboBoxModel<>(listaEstaciones));
		panel.add(jComboBoxDestino);
		// jComboBoxDestino.addActionListener(this);

		// Boton para procesar petici�n
		botonAccion = new JButton();
		botonAccion.setText("Procesar peticion");
		botonAccion.setBounds(630, 130, 150, 40);
		panel.add(botonAccion);
		botonAccion.addActionListener(this);

		

		// Literal de Numero de trasbordos
		JLabel label6 = new JLabel("Numero de trasbordos:");
		panel.add(label6);
		Dimension size6 = label6.getPreferredSize();
		label6.setBounds(630, 340, size6.width, size6.height);

		// Resultado de numero de trasbordos
		numeroTransbordos = new JTextField();
		numeroTransbordos.setBounds(630, 360, 150, 30);
		numeroTransbordos.setEditable(false);
		panel.add(numeroTransbordos);

		// Literal de estaciones recorridas
		JLabel label7 = new JLabel("Recorrido:");
		panel.add(label7);
		Dimension size7 = label7.getPreferredSize();
		label7.setBounds(630, 400, size7.width, size7.height);

		// Lista de estaciones recorridas
		recorrido = new JTextPane();
		recorrido.setEditable(false);
		add(recorrido);

		scrollpanel = new JScrollPane();
		scrollpanel.setBounds(new Rectangle(630, 420, 150, 190));
		scrollpanel.setViewportView(recorrido);
		add(scrollpanel);

		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JLabel picLabel[] = new JLabel[58];
		for (int i = 0; i < 54; i++)
			picLabel[i] = null;

		repaint();
		planoFondo.removeAll();

		String EstacionSalida = (String) jComboBoxSalida.getSelectedItem();
		String EstacionDestino = (String) jComboBoxDestino.getSelectedItem();

		Recorrido reco = new Recorrido();
		try {

			reco = lineasMetroAtenas.ProcesaPeticion(EstacionSalida, EstacionDestino);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		String transbordos = Integer.toString((int) reco.trasbordos);
		String recoFinal = "";

		recorrido.setText(recoFinal);
		numeroTransbordos.setText(transbordos);
		StyledDocument doc = recorrido.getStyledDocument();
		Style estilo = recorrido.addStyle("Soy un estilo", null);

		Estacion estacion;
		int linea;
		Color color = Color.black;
		int i = 0;

		while (reco.recorridoEstaciones[i] != null) {
			if (i > 0) {
				StyleConstants.setForeground(estilo, color);
				try {
					doc.insertString(doc.getLength(), "\n", estilo);
				} catch (BadLocationException e) {
				}
			}
			try {
				estacion = lineasMetroAtenas.buscarEstacion(reco.recorridoEstaciones[i]);
				linea = estacion.getLinea();
				switch (linea) {
					case 1:
						color = Color.green.darker().darker();
						break;
					case 2:
						color = Color.red;
						break;
					case 3:
						color = Color.blue;
						break;
				}

			} catch (Exception exception) {
				exception.printStackTrace();
			}

			StyleConstants.setForeground(estilo, color);
			try {
				doc.insertString(doc.getLength(), reco.recorridoEstaciones[i], estilo);
			} catch (BadLocationException e) {
			}
			i++;
		}

		this.getContentPane().add(scrollpanel).setBounds(630, 420, 150, 190);
		scrollpanel.getViewport().setViewPosition(new Point(0, 0));

		repaint();

		int j = 0;
		while (reco.recorridoEstaciones[j] != null) {
			try {
				estacion = lineasMetroAtenas.buscarEstacion(reco.recorridoEstaciones[j]);
				if (j == 0) {
					picLabel[j] = new JLabel(puntoSalida);
				} else if (j == i - 1) {
					picLabel[j] = new JLabel(puntoDestino);
				} else {
					picLabel[j] = new JLabel(lineas);
				}
				picLabel[j].setBounds(estacion.getCoorX(), estacion.getCoorY(), 8, 8);
				picLabel[j].setOpaque(false);
				planoFondo.add(picLabel[j]);
				j++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}