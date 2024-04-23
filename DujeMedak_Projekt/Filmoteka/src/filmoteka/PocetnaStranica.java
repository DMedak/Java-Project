package filmoteka;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class PocetnaStranica {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PocetnaStranica window = new PocetnaStranica();
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
	public PocetnaStranica() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setBounds(100, 100, 530, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Filmoteka");
		lblNewLabel.setBackground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Shrikhand", Font.PLAIN, 50));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBounds(114, 44, 284, 70);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Novi film");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(255, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NoviFilm window = new NoviFilm();
				window.frame.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Shrikhand", Font.PLAIN, 20));
		btnNewButton.setBounds(55, 197, 160, 124);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnPosudiFilm = new JButton("Posudi \r\nfilm");
		btnPosudiFilm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PosudiFilm window = new PosudiFilm();
				window.frame.setVisible(true);
			}
		});
		btnPosudiFilm.setForeground(new Color(255, 255, 255));
		btnPosudiFilm.setBackground(new Color(255, 0, 0));
		btnPosudiFilm.setFont(new Font("Shrikhand", Font.PLAIN, 20));
		btnPosudiFilm.setBounds(299, 197, 160, 124);
		frame.getContentPane().add(btnPosudiFilm);
		
		JLabel lblNewLabel_1 = new JLabel("MARVEL");
		lblNewLabel_1.setBackground(new Color(255, 255, 255));
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setFont(new Font("Shrikhand", Font.BOLD, 40));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(124, 113, 262, 36);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton dodajclan = new JButton("Dodaj clana");
		dodajclan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clanovi window = new Clanovi();
				window.frame.setVisible(true);
			}
		});
		dodajclan.setForeground(Color.WHITE);
		dodajclan.setFont(new Font("Dialog", Font.PLAIN, 20));
		dodajclan.setBackground(Color.RED);
		dodajclan.setBounds(160, 355, 200, 86);
		frame.getContentPane().add(dodajclan);
	}
}
