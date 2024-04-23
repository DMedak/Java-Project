package filmoteka;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class Clanovi {

	JFrame frame;
	private JTextField ime;
	private JTextField prezime;
	private JTextField email;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Clanovi window = new Clanovi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Clanovi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(120, 214, 254));
		frame.setBounds(100, 100, 528, 403);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel labelime = new JLabel("Ime clana:");
		labelime.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelime.setBounds(30, 53, 161, 55);
		frame.getContentPane().add(labelime);
		
		JLabel labelprezime = new JLabel("Prezime clana:");
		labelprezime.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelprezime.setBounds(30, 115, 161, 55);
		frame.getContentPane().add(labelprezime);
		
		JLabel labelemail = new JLabel("Email:");
		labelemail.setFont(new Font("Dialog", Font.PLAIN, 20));
		labelemail.setBounds(30, 171, 161, 55);
		frame.getContentPane().add(labelemail);
		
		ime = new JTextField();
		ime.setFont(new Font("Dialog", Font.PLAIN, 16));
		ime.setBounds(214, 67, 260, 34);
		frame.getContentPane().add(ime);
		ime.setColumns(10);
		
		prezime = new JTextField();
		prezime.setFont(new Font("Dialog", Font.PLAIN, 16));
		prezime.setColumns(10);
		prezime.setBounds(214, 129, 260, 34);
		frame.getContentPane().add(prezime);
		
		email = new JTextField();
		email.setFont(new Font("Dialog", Font.PLAIN, 16));
		email.setColumns(10);
		email.setBounds(214, 185, 260, 34);
		frame.getContentPane().add(email);
		
		JButton btnDodajClana = new JButton("Dodaj clana");
		btnDodajClana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
                    String imes = ime.getText();
                    String prezimes = prezime.getText();
                    String emails = email.getText();
                  
                    dodajClanUBazu(imes, prezimes, emails);

                    JOptionPane.showMessageDialog(null, "Podaci su spremljeni u bazu podataka.");

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Neuspjesno spremanje podataka u bazu.");
                }
            }
		});
		
		
		btnDodajClana.setForeground(Color.WHITE);
		btnDodajClana.setFont(new Font("Dialog", Font.PLAIN, 20));
		btnDodajClana.setBackground(Color.RED);
		btnDodajClana.setBounds(170, 269, 176, 55);
		frame.getContentPane().add(btnDodajClana);
	}
	private void dodajClanUBazu(String imes, String prezimes, String emails){
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/dmedak?serverTimezone=UTC", "dmedak", "19");

            String upit = "INSERT INTO Clanovi (ime_clana, prezime_clana, email_clana) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(upit);
            ps.setString(1, imes);
            ps.setString(2, prezimes);
            ps.setString(3, emails);
            ps.executeUpdate();

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}

