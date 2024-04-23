package filmoteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NoviFilm {

	JFrame frame;
	private JTextField textField;
	private JTable table;
	private DefaultTableModel tableModel;
	private JComboBox<String> comboBox;
	private JTextField textField_1;
	private JTextField textField_2;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NoviFilm window = new NoviFilm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public NoviFilm() {
		initialize();
		refreshTablice();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setBounds(100, 100, 530, 520);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Naslov novog filma:");
		lblNewLabel.setFont(new Font("Shrikhand", Font.PLAIN, 16));
		lblNewLabel.setBounds(23, 11, 169, 53);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBackground(new Color(255, 255, 255));
		textField.setFont(new Font("Shrikhand", Font.PLAIN, 16));
		textField.setBounds(202, 19, 271, 37);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel sviFilmovi = new JLabel("Svi filmovi:");
		sviFilmovi.setFont(new Font("Shrikhand", Font.PLAIN, 15));
		sviFilmovi.setBounds(202, 226, 99, 37);
		frame.getContentPane().add(sviFilmovi);

		JButton dodajFilm = new JButton("Dodaj");
		dodajFilm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String naslov = textField.getText();
					String godina = (String) comboBox.getSelectedItem();
					String redatelj = textField_1.getText();
					double imdbOcjena = Double.parseDouble(textField_2.getText());

					dodajFilmUBazu(naslov, godina, redatelj, imdbOcjena);

					JOptionPane.showMessageDialog(null, "Podaci su spremljeni u bazu podataka.");

					refreshTablice();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Unesite valjane podatke za IMDb ocjenu.");
				}
			}
		});
		dodajFilm.setBackground(new Color(255, 0, 0));
		dodajFilm.setFont(new Font("Shrikhand", Font.PLAIN, 20));
		dodajFilm.setBounds(175, 192, 126, 35);
		frame.getContentPane().add(dodajFilm);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 261, 438, 196);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Shrikhand", Font.PLAIN, 15));
		scrollPane.setViewportView(table);

		tableModel = new DefaultTableModel();
		tableModel.addColumn("ID filma");
		tableModel.addColumn("Naslov filma");
		tableModel.addColumn("Godina izdanja");
		tableModel.addColumn("Redatelj");
		tableModel.addColumn("IMDb ocjena");
		table.setModel(tableModel);

		JLabel lblGodinaFilma = new JLabel("Godina filma:");
		lblGodinaFilma.setFont(new Font("Shrikhand", Font.PLAIN, 16));
		lblGodinaFilma.setBounds(23, 58, 169, 37);
		frame.getContentPane().add(lblGodinaFilma);

		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Shrikhand", Font.PLAIN, 15));
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "2023", "2022", "2021", "2020", "2019", "2018",
				"2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
				"2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995" }));
		comboBox.setMaximumRowCount(30);
		comboBox.setBounds(202, 60, 144, 35);
		frame.getContentPane().add(comboBox);

		JLabel redatelj = new JLabel("Redatelj filma:");
		redatelj.setFont(new Font("Dialog", Font.PLAIN, 16));
		redatelj.setBounds(23, 99, 169, 37);
		frame.getContentPane().add(redatelj);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.PLAIN, 16));
		textField_1.setColumns(10);
		textField_1.setBackground(Color.WHITE);
		textField_1.setBounds(202, 99, 271, 37);
		frame.getContentPane().add(textField_1);

		JLabel imdbOcjena = new JLabel("IMDB ocjena filma:");
		imdbOcjena.setFont(new Font("Dialog", Font.PLAIN, 16));
		imdbOcjena.setBounds(23, 144, 169, 37);
		frame.getContentPane().add(imdbOcjena);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Dialog", Font.PLAIN, 16));
		textField_2.setColumns(10);
		textField_2.setBackground(Color.WHITE);
		textField_2.setBounds(202, 144, 86, 37);
		frame.getContentPane().add(textField_2);

		refreshTablice();
	}

	private void dodajFilmUBazu(String naslov, String godina, String redatelj, double imdbOcjena) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC",
					"dmedak", "19");

			String upit = "INSERT INTO Filmovi (naziv_filma, godina_izdanja, redatelj, IMDB_ocjena) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(upit);
			ps.setString(1, naslov);
			ps.setString(2, godina);
			ps.setString(3, redatelj);
			ps.setDouble(4, imdbOcjena);
			ps.executeUpdate();

			con.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
	}

	private void refreshTablice() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC",
					"dmedak", "19");

			String upit = "SELECT film_id, naziv_filma, godina_izdanja, redatelj, IMDB_ocjena FROM Filmovi";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(upit);

			tableModel.setRowCount(0);

			while (rs.next()) {
				int idFilma = rs.getInt("film_id");
				String naslov = rs.getString("naziv_filma");
				String godina = rs.getString("godina_izdanja");
				String redatelj = rs.getString("redatelj");
				double imdbOcjena = rs.getDouble("IMDB_ocjena");
				tableModel.addRow(new Object[] { idFilma, naslov, godina, redatelj, imdbOcjena });
			}

			con.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}
