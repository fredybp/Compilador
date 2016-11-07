package gui;

import static main.MaquinaVirtual.executaInstrucoes;
import static main.MaquinaVirtual.montaDadosTabelaInstrucao;
import static main.MaquinaVirtual.procurarArquivo;

import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TelaPrincipal extends JFrame {
	
	/*
	 *  TODO: 
	 *  getJanelaEntrada();
	 */

	private static final long serialVersionUID = 8206910973434962454L;
	private static JTable tableInstrucoes = new JTable();
	private static JTable tableDados = new JTable();
	private static TelaPrincipal frame = new TelaPrincipal();
	private JPanel contentPane;
	private JLabel lblJanelaDeEntrada;
	private JLabel lblJanelaDeSaida;
	private JLabel lblBreakPoints;
	private JLabel lblInstruesASeremExecutadas;
	private JLabel lblContedoDaPilha;
	private static JScrollPane scrollJanelaSaida;
	private JScrollPane scrollTableDados;
	private JScrollPane scrollBreakPoints;
	private JScrollPane scrollTableInstrucoes;
	private JScrollPane scrollJanelaEntrada;
	private static JTextArea janelaSaida;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 467);

		// Jscroll
		scrollTableInstrucoes = new JScrollPane();
		scrollTableDados = new JScrollPane();
		scrollJanelaEntrada = new JScrollPane();
		scrollJanelaSaida = new JScrollPane();
		scrollBreakPoints = new JScrollPane();

		// Labels
		lblInstruesASeremExecutadas = new JLabel("Instru\u00E7\u00F5es a serem executadas pela VM");
		lblJanelaDeEntrada = new JLabel("Janela de Entrada");
		lblJanelaDeSaida = new JLabel("Janela de Saida");
		lblBreakPoints = new JLabel("Break Point's");
		lblContedoDaPilha = new JLabel("Conte\u00FAdo da Pilha");

		inicializaContentPane();
		inicializaBarraBotoes(scrollTableInstrucoes);
		inicializaJanelaEntrada(scrollJanelaEntrada);
		inicializaTabelaDados(scrollTableDados);
		inicializaTabelaInstrucoes(scrollTableInstrucoes);
		inicializaJanelaSaida(scrollJanelaSaida);

		inicializaLayout(scrollTableInstrucoes, scrollTableDados, scrollJanelaEntrada, scrollJanelaSaida,
				scrollBreakPoints, lblInstruesASeremExecutadas, lblJanelaDeEntrada, lblJanelaDeSaida, lblBreakPoints,
				lblContedoDaPilha);
	}

	private void inicializaContentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	}

	private void inicializaBarraBotoes(JScrollPane scrollTableInstrucoes) {
		JMenuBar menuBar = montaBarraMenu();
		inicializaBotaoAbrirArquivo(scrollTableInstrucoes, menuBar);
		inicializaBotaoExecutar(menuBar);
		inicializaBotaoContinuar(menuBar);
	}

	private void inicializaBotaoContinuar(JMenuBar menuBar) {
		JMenuItem menuContinuar = new JMenuItem("Continuar");
		menuBar.add(menuContinuar);
	}

	private void inicializaBotaoExecutar(JMenuBar menuBar) {
		JMenuItem menuExecutar = new JMenuItem("Executar");
		menuBar.add(menuExecutar);
		menuExecutar.addActionListener(event -> chamaExecutaInstrucoes(scrollTableDados));
	}

	private static void chamaExecutaInstrucoes(JScrollPane scrollTableDados) {
		try {
			tableDados.setModel(executaInstrucoes());
			scrollTableDados.setViewportView(tableDados);
			frame.getContentPane().add(scrollTableDados);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void inicializaJanelaEntrada(JScrollPane scrollJanelaEntrada) {
		JTextPane janelaEntrada = new JTextPane();
		scrollJanelaEntrada.setViewportView(janelaEntrada);
	}
	
	private void inicializaJanelaSaida(JScrollPane scrollJanelaSaida) {
		janelaSaida = new JTextArea();
		janelaSaida.setEditable(false);
		scrollJanelaSaida.setViewportView(janelaSaida);
	}
	
	private JMenuBar montaBarraMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		return menuBar;
	}

	private void inicializaBotaoAbrirArquivo(JScrollPane scrollTableInstrucoes, JMenuBar menuBar) {
		JMenuItem menuAbrirArquivo = new JMenuItem("Abrir Arquivo...");
		menuBar.add(menuAbrirArquivo);
		menuAbrirArquivo.addActionListener(event -> {
			procurarArquivo(frame);
			try {
				preencheTabelaInstrucoes(scrollTableInstrucoes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void inicializaTabelaInstrucoes(JScrollPane scrollTableInstrucoes) {
		tableInstrucoes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Linha", "Label", "Mnemonico", "Parametro 1", "Parametro 2" }));
		scrollTableInstrucoes.setViewportView(tableInstrucoes);
	}

	private void inicializaTabelaDados(JScrollPane scrollTableDados) {
		tableDados.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Endere�o ", "Valor" }));
		scrollTableDados.setViewportView(tableDados);
	}

	private void inicializaLayout(JScrollPane scrollTableInstrucoes, JScrollPane scrollTableDados,
			JScrollPane scrollJanelaEntrada, JScrollPane scrollJanelaSaida, JScrollPane scrollBreakPoints,
			JLabel lblInstruesASerem, JLabel lblJanelaDeEntrada, JLabel lblJanelaDeSaida, JLabel lblBreakPoints,
			JLabel lblContedoDaPilha) {
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
								gl_contentPane
										.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
												.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
														.createSequentialGroup().addGroup(gl_contentPane
																.createParallelGroup(Alignment.TRAILING).addComponent(
																		scrollTableInstrucoes, GroupLayout.DEFAULT_SIZE,
																		408, Short.MAX_VALUE)
																.addGroup(gl_contentPane
																		.createSequentialGroup()
																		.addGroup(gl_contentPane
																				.createParallelGroup(Alignment.LEADING)
																				.addComponent(scrollJanelaEntrada,
																						GroupLayout.PREFERRED_SIZE, 145,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(lblJanelaDeEntrada))
																		.addGap(18)
																		.addGroup(gl_contentPane
																				.createParallelGroup(Alignment.LEADING)
																				.addComponent(scrollJanelaSaida,
																						GroupLayout.PREFERRED_SIZE, 145,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(lblJanelaDeSaida))
																		.addPreferredGap(ComponentPlacement.RELATED, 16,
																				Short.MAX_VALUE)
																		.addGroup(gl_contentPane
																				.createParallelGroup(Alignment.LEADING)
																				.addComponent(lblBreakPoints)
																				.addComponent(scrollBreakPoints,
																						GroupLayout.PREFERRED_SIZE, 84,
																						GroupLayout.PREFERRED_SIZE))))
														.addPreferredGap(ComponentPlacement.RELATED,
																15, Short.MAX_VALUE))
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(lblInstruesASeremExecutadas).addPreferredGap(
																ComponentPlacement.RELATED)))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(scrollTableDados, GroupLayout.PREFERRED_SIZE, 147,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblContedoDaPilha, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInstruesASeremExecutadas).addComponent(lblContedoDaPilha))
				.addGap(7)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollTableDados, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(scrollTableInstrucoes, GroupLayout.PREFERRED_SIZE, 239,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblJanelaDeSaida).addComponent(lblBreakPoints)
										.addComponent(lblJanelaDeEntrada))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollBreakPoints, GroupLayout.PREFERRED_SIZE, 93,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollJanelaSaida, GroupLayout.PREFERRED_SIZE, 93,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollJanelaEntrada, GroupLayout.PREFERRED_SIZE, 93,
												GroupLayout.PREFERRED_SIZE))
								.addGap(4)))
				.addContainerGap()));

		contentPane.setLayout(gl_contentPane);
	}

	private static void preencheTabelaInstrucoes(JScrollPane scrollTableInstrucoes) throws Exception {
		tableInstrucoes.setModel(montaDadosTabelaInstrucao());
		scrollTableInstrucoes.setViewportView(tableInstrucoes);
		frame.getContentPane().add(scrollTableInstrucoes);
	}

	public static void printJanelaSaida(Integer valor) {
		janelaSaida.append(valor.toString() + "\n");
		scrollJanelaSaida.setViewportView(janelaSaida);
		frame.getContentPane().add(scrollJanelaSaida);	
	}
	
	public static Integer getJanelaEntrada() {
		
		return null;
	}

}