import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
//import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTable;

public class VentanaPrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean flag = true;
	
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTable table;
	//private static RSyntaxTextArea textArea;
	private JTabbedPane tabbedPane_1;
	private HashMap<RTextScrollPane,Path> pestanas= new HashMap<RTextScrollPane,Path>();
	private static RSyntaxTextArea textAreaInicial;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setTitle("Own Editor - Nuevo archivo");
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					textAreaInicial.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 845, 486);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				flag=true;
				setTitle("Own Editor - Archivo nuevo");
				lblNewLabel.setText("Nuevo");
				RTextScrollPane sp = new RTextScrollPane(crearTextArea(""));
				tabbedPane_1.addTab("nuevo", null, sp, null);
				pestanas.put(sp,null);
				tabbedPane_1.setSelectedIndex(tabbedPane_1.getTabCount()-1);
				//tabbedPane_1.setTabComponentAt(tabbedPane_1.getTabCount(), new JButton(tabbedPane_1, this));
			}
		});
		mnArchivo.add(mntmNuevo);

		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abrirArchivo();
			}
		});
		mnArchivo.add(mntmAbrir);

		mnArchivo.addSeparator();

		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}
		});
		mnArchivo.add(mntmGuardar);

		JMenuItem mntmGuardarComo = new JMenuItem("Guardar como");
		mntmGuardarComo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showSaveDialog(contentPane);
				if (result == JFileChooser.APPROVE_OPTION) {
					System.out.println(fileChooser.getSelectedFile().getName());
					try  {
						
						Path path=fileChooser.getSelectedFile().toPath();
						File file = new File(path.toString());

						// if file doesnt exists, then create it
						if (!file.exists()) {
							file.createNewFile();
						}
						pestanas.put(getScrollPaneActual(),path);
						tabbedPane_1.setTitleAt(tabbedPane_1.getSelectedIndex(),file.getName());
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(getTextAreaActual().getText());
						bw.close();
						System.out.println("Done");
					}catch(Exception ee){

					}
					System.out.println((fileChooser.getSelectedFile().getName()));
					System.out.println(fileChooser.getCurrentDirectory().toString());
				}
				if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("You pressed cancel");

				}
				setTitle("Own Editor - "+fileChooser.getSelectedFile().getName());
				lblNewLabel.setText("Archivo guardado");
				flag=false;				
			}
		});
		mnArchivo.add(mntmGuardarComo);

		mnArchivo.addSeparator();

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnArchivo.add(mntmSalir);

		JMenu mnPreferencias = new JMenu("Preferencias");
		menuBar.add(mnPreferencias);

		JMenu mnTemas = new JMenu("Temas");
		mnPreferencias.add(mnTemas);

		JMenuItem mntmClaro = new JMenuItem("Claro");
		mntmClaro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setBackground(SystemColor.white);
					textArea.setForeground(SystemColor.black);
					textArea.setCurrentLineHighlightColor(SystemColor.controlHighlight);
					textArea.revalidate();	
				}
				contentPane.setBackground(new Color(240,240,240));
				lblNewLabel.setForeground(Color.black);
				lblNewLabel_1.setForeground(Color.black);
				revalidate();
				repaint();
			}
		});
		mntmClaro.setSelected(true);
		mnTemas.add(mntmClaro);

		JMenuItem mntmOscuro = new JMenuItem("Oscuro");
		mntmOscuro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setBackground(SystemColor.windowBorder);
					textArea.setForeground(SystemColor.white);
					textArea.setCurrentLineHighlightColor(SystemColor.DARK_GRAY);
					textArea.revalidate();
							
				}
				contentPane.setBackground(SystemColor.DARK_GRAY);
				lblNewLabel.setForeground(Color.white);
				lblNewLabel_1.setForeground(Color.white);
				revalidate();
				repaint();	
				
			}
		});
		mnTemas.add(mntmOscuro);

		JMenu mnTamaoDeLetra = new JMenu("Tama\u00F1o de letra");
		mnPreferencias.add(mnTamaoDeLetra);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("9");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setFont(new Font("Consolas", Font.PLAIN, 9));
					textArea.revalidate();
					textArea.repaint();				
				}
			}
		});
		mnTamaoDeLetra.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("11");
		mntmNewMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setFont(new Font("Consolas", Font.PLAIN, 11));
					textArea.revalidate();
					textArea.repaint();				
				}			
			}
		});
		mnTamaoDeLetra.add(mntmNewMenuItem);

		JMenuItem menuItem = new JMenuItem("13");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
					textArea.revalidate();
					textArea.repaint();				
				}		
			}
		});
		menuItem.setSelected(true);
		mnTamaoDeLetra.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("15");
		menuItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setFont(new Font("Consolas", Font.PLAIN, 15));
					textArea.revalidate();
					textArea.repaint();				
				}			
			}
		});
		mnTamaoDeLetra.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem("17");
		menuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextArea textArea;
				for(RTextScrollPane scroll:pestanas.keySet()){
					textArea=((RSyntaxTextArea)scroll.getViewport().getView());
					textArea.setFont(new Font("Consolas", Font.PLAIN, 17));
					textArea.revalidate();
					textArea.repaint();				
				}
			}
		});
		mnTamaoDeLetra.add(menuItem_2);

		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);

		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mnAyuda.add(mntmAcercaDe);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 181, 0, 113, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		RSyntaxTextArea textArea;
		textArea = new RSyntaxTextArea(20, 60);
		textArea.setFocusable(true);

		textArea.requestFocus();
		textArea.requestFocusInWindow();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		// Add an item to the popup menu that opens the file whose name is
		// specified at the current caret position.
		JPopupMenu popup = textArea.getPopupMenu();

		textArea.setCodeFoldingEnabled(true);
		textArea.setPopupMenu(popup);
		textArea.setCurrentLineHighlightColor(SystemColor.controlHighlight);
		
		JButton btnNewButton = new JButton("Abrir");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirArchivo();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		JButton btnNewButton_1 = new JButton("Guardar");
		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 0;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);

		JButton btnALexico = new JButton("A. Lexico");
		btnALexico.setEnabled(false);
		GridBagConstraints gbc_btnALexico = new GridBagConstraints();
		gbc_btnALexico.insets = new Insets(0, 0, 5, 5);
		gbc_btnALexico.gridx = 2;
		gbc_btnALexico.gridy = 0;
		contentPane.add(btnALexico, gbc_btnALexico);

		JButton btnASintactico = new JButton("A. Sintactico");
		btnASintactico.setEnabled(false);		
		GridBagConstraints gbc_btnASintactico = new GridBagConstraints();
		gbc_btnASintactico.insets = new Insets(0, 0, 5, 5);
		gbc_btnASintactico.gridx = 3;
		gbc_btnASintactico.gridy = 0;
		contentPane.add(btnASintactico, gbc_btnASintactico);

		JButton btnASemantico = new JButton("A. Semantico");
		btnASemantico.setEnabled(false);		
		GridBagConstraints gbc_btnASemantico = new GridBagConstraints();
		gbc_btnASemantico.insets = new Insets(0, 0, 5, 5);
		gbc_btnASemantico.gridx = 4;
		gbc_btnASemantico.gridy = 0;
		contentPane.add(btnASemantico, gbc_btnASemantico);

		JButton btnGi = new JButton("GI");
		btnGi.setEnabled(false);		
		GridBagConstraints gbc_btnGi = new GridBagConstraints();
		gbc_btnGi.insets = new Insets(0, 0, 5, 5);
		gbc_btnGi.gridx = 5;
		gbc_btnGi.gridy = 0;
		contentPane.add(btnGi, gbc_btnGi);

		JButton btnEjecucion = new JButton("Ejecucion");
		btnEjecucion.setEnabled(false);		
		GridBagConstraints gbc_btnEjecucion = new GridBagConstraints();
		gbc_btnEjecucion.anchor = GridBagConstraints.WEST;
		gbc_btnEjecucion.insets = new Insets(0, 0, 5, 0);
		gbc_btnEjecucion.gridx = 6;
		gbc_btnEjecucion.gridy = 0;
		contentPane.add(btnEjecucion, gbc_btnEjecucion);
		
		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane_1 = new GridBagConstraints();
		gbc_tabbedPane_1.gridwidth = 7;
		gbc_tabbedPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane_1.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane_1.gridx = 0;
		gbc_tabbedPane_1.gridy = 1;
		contentPane.add(tabbedPane_1, gbc_tabbedPane_1);
		
		textAreaInicial=textArea;
		RTextScrollPane sp = new RTextScrollPane(textArea);
		tabbedPane_1.addTab("New tab", null, sp, null);
	    pestanas.put(sp,null);
		
		

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 2;
		gbc_tabbedPane.gridwidth = 7;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 2;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Errores/Advertencias", null, scrollPane, null);
		
		table = new JTable();
		
		scrollPane.setViewportView(table);

		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Salida/Resultados", null, scrollPane_1, null);

		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);

		lblNewLabel = new JLabel("En espera");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 5;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 4;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		lblNewLabel_1 = new JLabel("(1:2)");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.gridx = 6;
		gbc_lblNewLabel_1.gridy = 4;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
	}
	void abrirArchivo(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(contentPane);
		RTextScrollPane sp=null;
				if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if(!pestanas.values().contains(selectedFile.toPath())){
				RTextScrollPane tempScrollPane;
				try {
					StringBuilder sb = new StringBuilder();
					FileReader reader = new FileReader(selectedFile.getAbsolutePath());
					int character;
					while ((character = reader.read()) != -1) 
						sb.append((char) character);
					File filetemp=fileChooser.getSelectedFile();
					
					setTitle("Editor Fresita xD");
					if(pestanas.get(tempScrollPane=getScrollPaneActual())==null){
						getTextAreaActual().setText(sb.toString());
						pestanas.put(tempScrollPane,filetemp.toPath());
						tabbedPane_1.setTitleAt(tabbedPane_1.getSelectedIndex(),fileChooser.getSelectedFile().getName());
					}else{
						sp = new RTextScrollPane(crearTextArea(sb.toString()));
						tabbedPane_1.addTab(fileChooser.getSelectedFile().getName(), null, sp, null);
						pestanas.put(sp,fileChooser.getSelectedFile().toPath());
						tabbedPane_1.setSelectedIndex(tabbedPane_1.getTabCount()-1);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(null,"El archivo ya esta abierto!");
			}
		}
		flag=false;
		lblNewLabel.setText("Archivo abierto");
	}
	
	
	void guardarArchivo(){
		if(flag){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showSaveDialog(contentPane);
			if (result == JFileChooser.APPROVE_OPTION) {
				System.out.println(fileChooser.getSelectedFile().getName());
				try  {
					Path path=fileChooser.getSelectedFile().toPath();
					File file = new File(path.toString());
					
					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
					pestanas.put(getScrollPaneActual(),path);
					tabbedPane_1.setTitleAt(tabbedPane_1.getSelectedIndex(),file.getName());
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(getTextAreaActual().getText());
					bw.close();
					System.out.println("Done");
				}catch(Exception ee){

				}
				System.out.println((fileChooser.getSelectedFile().getName()));
				System.out.println(fileChooser.getCurrentDirectory().toString());
			}
			if (result == JFileChooser.CANCEL_OPTION) {
				System.out.println("You pressed cancel");

			}
			flag=false;
			setTitle("Own Editor - "+fileChooser.getSelectedFile().getName());
		}else{
			try{
				File file = new File(pestanas.get(getScrollPaneActual()).toString());
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(getTextAreaActual().getText());
				bw.close();
				
				System.out.println("Done");
			}catch(Exception eee){
				eee.printStackTrace();
			}
		}
		lblNewLabel.setText("Archivo guardado");
	}
	

	
	public RTextScrollPane getScrollPaneActual(){
		return (RTextScrollPane)tabbedPane_1.getSelectedComponent();
	}
	
	public RSyntaxTextArea getTextAreaActual(){
		return ((RSyntaxTextArea)getScrollPaneActual().getViewport().getView());
	}
	
	RSyntaxTextArea crearTextArea(String text){
		RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
		textArea.setFocusable(true);

		textArea.requestFocus();
		textArea.requestFocusInWindow();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		// Add an item to the popup menu that opens the file whose name is
		// specified at the current caret position.
		JPopupMenu popup = textArea.getPopupMenu();

		textArea.setCodeFoldingEnabled(true);
		textArea.setPopupMenu(popup);
		textArea.setCurrentLineHighlightColor(SystemColor.controlHighlight);
		textArea.setText(text);
		return textArea;
	}
}
