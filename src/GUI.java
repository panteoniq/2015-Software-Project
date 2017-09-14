package n_dep_n_land;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Scrollbar;

import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import java.awt.Label;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class GUI extends JFrame {
//	private Thread DDangKong;
	private JPanel contentPane;
	private JTextField minInput;
	private JTextField inputPer;
	private Thread DDangKong;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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


	public GUI() {
		DDangKong=null;
		setResizable(false);
		Integer[] speedVal={1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		setTitle("No Departure, No Landing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1368, 738);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel input = new JPanel();
		input.setBounds(0, 0, 1362, 96);
		contentPane.add(input);
		input.setLayout(null);

		JLabel inputTime = new JLabel("시뮬레이션 시간 입력(단위 : 분)");
		inputTime.setBounds(361, 13, 182, 15);
		input.add(inputTime);

		JLabel inputSpeed = new JLabel("\uBC30\uC18D \uC120\uD0DD(1000/x)(ms)");
		inputSpeed.setBounds(361, 38, 135, 15);
		input.add(inputSpeed);

		minInput = new JTextField();
		minInput.setText("1440");
		minInput.setBounds(555, 10, 116, 21);
		input.add(minInput);
		minInput.setColumns(10);

		JComboBox selectSpeedVal = new JComboBox();
		selectSpeedVal.setModel(new DefaultComboBoxModel(speedVal));
		selectSpeedVal.setBounds(555, 38, 65, 21);
		input.add(selectSpeedVal);

		JButton start = new JButton("시작");

		start.setBounds(712, 23, 97, 45);
		input.add(start);

		JButton exit = new JButton("종료");
		exit.setBounds(833, 23, 97, 45);
		input.add(exit);

		inputPer = new JTextField("33");
		inputPer.setBounds(555, 66, 116, 21);
		input.add(inputPer);
		inputPer.setColumns(10);

		JLabel inputPercent = new JLabel("\uC774/\uCC29\uB959 \uD37C\uC13C\uD2B8(\uB2E8\uC704 :%)");
		inputPercent.setBounds(361, 69, 135, 15);
		input.add(inputPercent);

		JButton btnNewButton = new JButton("\uB3C4\uC6C0\uB9D0");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"시뮬레이션 시간 : 시뮬레이션을 수행할 시간을 설정합니다"+"\n"+"배속 : 시뮬레이션의 속도를 결정합니다"+"\n"+
						"이/착륙 퍼센트 : 입력한 x%의 확률로 단위 시간마다 비행기가 1~3대씩 들어옵니다", "정보", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(948, 23, 97, 45);
		input.add(btnNewButton);

		JPanel progress = new JPanel();
		progress.setBounds(975, 96, 387, 604);
		contentPane.add(progress);
		progress.setLayout(null);

		JLabel progressPrint = new JLabel("\uC0C1\uD669 \uCD9C\uB825");
		progressPrint.setForeground(new Color(255, 0, 0));
		progressPrint.setFont(new Font("굴림", Font.BOLD, 16));
		progressPrint.setBounds(167, 10, 79, 15);
		progress.add(progressPrint);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(0, 0, 41, 635);
		progress.add(separator_3);
		separator_3.setForeground(new Color(0, 0, 0));
		separator_3.setBackground(new Color(0, 0, 0));
		separator_3.setOrientation(SwingConstants.VERTICAL);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(0, 0, 387, 43);
		progress.add(separator_4);
		separator_4.setForeground(new Color(0, 0, 0));
		separator_4.setBackground(new Color(0, 0, 0));
		JScrollPane p_progress_s = new JScrollPane();
		p_progress_s.setBounds(23, 44, 352, 550);
		progress.add(p_progress_s);
		
				JTextArea p_progress = new JTextArea();
				p_progress_s.setViewportView(p_progress);
				p_progress.setEditable(false);

		JPanel airport = new JPanel();
		airport.setBackground(new Color(255, 255, 255));
		airport.setBounds(0, 96, 975, 604);
		contentPane.add(airport);
		airport.setLayout(null);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 0, 975, 2);
		airport.add(separator_1);
		separator_1.setForeground(new Color(0, 0, 0));
		separator_1.setBackground(new Color(0, 0, 0));

		JLabel r_1 = new JLabel("New label");
		r_1.setIcon(new ImageIcon("D:\\\uB0B4 \uD30C\uC77C\\\uAE08\uC624\uACF5\uB300\\\uCAD8\uBC14\\softProj3\\images\\20151115_203045.jpg"));
		r_1.setBounds(108, 106, 98, 102);
		airport.add(r_1);

		JLabel r_2 = new JLabel("New label");
		r_2.setIcon(new ImageIcon("D:\\\uB0B4 \uD30C\uC77C\\\uAE08\uC624\uACF5\uB300\\\uCAD8\uBC14\\softProj3\\images\\20151115_203045.jpg"));
		r_2.setBounds(424, 106, 98, 102);
		airport.add(r_2);

		JLabel r_3 = new JLabel("New label");
		r_3.setIcon(new ImageIcon("D:\\\uB0B4 \uD30C\uC77C\\\uAE08\uC624\uACF5\uB300\\\uCAD8\uBC14\\softProj3\\images\\20151115_203045.jpg"));
		r_3.setBounds(757, 106, 98, 102);
		airport.add(r_3);

		JLabel emergency = new JLabel("New label");
		emergency.setIcon(new ImageIcon("D:\\\uB0B4 \uD30C\uC77C\\\uAE08\uC624\uACF5\uB300\\\uCAD8\uBC14\\softProj3\\images\\20151115_203439.jpg"));
		emergency.setBounds(767, 39, 74, 44);
		airport.add(emergency);

		JLabel r_1_dlabel = new JLabel("\uC774\uB959 \uD050");
		r_1_dlabel.setBounds(24, 242, 60, 15);
		airport.add(r_1_dlabel);

		JLabel r_1_llabel = new JLabel("\uCC29\uB959 \uD050");
		r_1_llabel.setBounds(24, 355, 57, 15);
		airport.add(r_1_llabel);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.BLACK);
		separator_5.setBackground(Color.BLACK);
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setBounds(318, 0, 2, 635);
		airport.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBackground(Color.BLACK);
		separator_6.setForeground(Color.BLACK);
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(638, 0, 23, 635);
		airport.add(separator_6);

		JLabel r_2_dlabel = new JLabel("\uC774\uB959 \uD050");
		r_2_dlabel.setBounds(367, 242, 57, 15);
		airport.add(r_2_dlabel);

		JLabel r_2_llabel = new JLabel("\uCC29\uB959 \uD050");
		r_2_llabel.setBounds(367, 355, 57, 15);
		airport.add(r_2_llabel);

		JLabel r_3_dlabel = new JLabel("\uC774\uB959 \uBC0F \uAE34\uAE09 \uCC29\uB959 \uD050");
		r_3_dlabel.setBounds(684, 242, 185, 15);
		airport.add(r_3_dlabel);

		JLabel la1 = new JLabel("1\uBC88 \uD65C\uC8FC\uB85C");
		la1.setFont(new Font("굴림", Font.BOLD, 14));
		la1.setBounds(108, 8, 81, 15);
		airport.add(la1);

		JLabel la2 = new JLabel("2\uBC88 \uD65C\uC8FC\uB85C");
		la2.setFont(new Font("굴림", Font.BOLD, 14));
		la2.setBounds(440, 8, 82, 15);
		airport.add(la2);

		JLabel la3 = new JLabel("3\uBC88 \uD65C\uC8FC\uB85C");
		la3.setFont(new Font("굴림", Font.BOLD, 14));
		la3.setBounds(757, 7, 98, 15);
		airport.add(la3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(688, 267, 223, 215);
		airport.add(scrollPane);

		JTextArea dep_sp3 = new JTextArea();
		dep_sp3.setEditable(false);
		scrollPane.setViewportView(dep_sp3);

		JScrollPane dep_sp2 = new JScrollPane();
		dep_sp2.setBounds(367, 267, 223, 78);
		airport.add(dep_sp2);

		JTextArea dep_ta2 = new JTextArea();
		dep_ta2.setEditable(false);
		dep_sp2.setViewportView(dep_ta2);

		JScrollPane land_sp3 = new JScrollPane();
		land_sp3.setBounds(367, 380, 223, 102);
		airport.add(land_sp3);

		JTextArea land_ta3 = new JTextArea();
		land_ta3.setEditable(false);
		land_sp3.setViewportView(land_ta3);

		JScrollPane dep_sp1 = new JScrollPane();
		dep_sp1.setBounds(34, 267, 233, 78);
		airport.add(dep_sp1);

		JTextArea dep_ta1 = new JTextArea();
		dep_ta1.setEditable(false);
		dep_sp1.setViewportView(dep_ta1);

		JScrollPane land_sp1 = new JScrollPane();
		land_sp1.setBounds(34, 380, 233, 102);
		airport.add(land_sp1);

		JTextArea land_ta1 = new JTextArea();
		land_ta1.setEditable(false);
		land_sp1.setViewportView(land_ta1);

		JScrollPane land_sp2 = new JScrollPane();
		land_sp2.setBounds(34, 499, 233, 102);
		airport.add(land_sp2);

		JTextArea land_ta2 = new JTextArea();
		land_ta2.setEditable(false);
		land_sp2.setViewportView(land_ta2);

		JScrollPane land_sp4 = new JScrollPane();
		land_sp4.setBounds(367, 499, 223, 102);
		airport.add(land_sp4);

		JTextArea land_ta4 = new JTextArea();
		land_ta4.setEditable(false);
		land_sp4.setViewportView(land_ta4);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//착륙 ,이륙 앞의 약자만 따옴 ,D_L 클래스가 Runnable을 구현한 클래스 
				try
				{
					p_progress.setText("");
					Depart_Land.PlayTime = Integer.parseInt(minInput.getText());//입력 시간 받고
					if (Integer.parseInt(minInput.getText())>10800)
					{
						JOptionPane.showMessageDialog(null,"시간은 10800분 미만으로 입력해야 합니다", "오류", 
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					else if (Integer.parseInt(minInput.getText())<=0)
					{
						JOptionPane.showMessageDialog(null,"시간은 1분 이상으로 입력해야 합니다", "오류", 
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					else if (Integer.parseInt(inputPer.getText())>100)
					{
						JOptionPane.showMessageDialog(null,"확률은 100% 이하로 입력해야 합니다", "오류", 
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					else if (Integer.parseInt(inputPer.getText())<=0)
					{
						JOptionPane.showMessageDialog(null,"확률은 1% 이상으로 입력해야 합니다", "오류", 
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					int index=selectSpeedVal.getSelectedIndex();
					Depart_Land.Speed = speedVal[index].intValue(); //배속 선택받고
					Depart_Land.percentage=Integer.parseInt(inputPer.getText());//퍼센트 수정하고
					Depart_Land DL = new Depart_Land(dep_ta1, land_ta1, land_ta2, dep_ta2, land_ta3, land_ta4, dep_sp3,p_progress, start); // 스레드 객체 생성
					DDangKong = new Thread(DL);  // 스레드 변수
					start.setEnabled(false);
					DDangKong.start();  // 시물레이션 스타트 !
					
				}
				catch (NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(null,"시간과 배속은 숫자를 입력하셔야 합니다!", "오류", 
							JOptionPane.WARNING_MESSAGE);
					start.setEnabled(true);
					return;
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});


	}
}
