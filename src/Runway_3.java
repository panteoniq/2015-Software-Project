package n_dep_n_land;

import java.util.Vector;

import javax.swing.JTextArea;
public class Runway_3 implements Runnable {
	private Vector<Airplane> dep_eland_Q;//�̷� �� ��� ���� ť
	private int q_ind;//�ش� ť�� ���� �ε���

	private String q_enq_status="";//Enqueue �޽���
	private String q_emer_enq_status="";//��� ���� ����� Enqueue �޽���
	private String q_deq_status="";//Dequeue �޽���

	private int dep_count;
	private int real_depcount;//��¥ �̷��� ����� ��
	private int emer_count;//��� ������ �̷��� �� ť���� �̷������ ������
	//���ڸ� ����� ��
	private int dep_waitAver;//�̷� ������� ��� ��� �ð�
	private int emer_wtime;//��� ���� ������� ��� ��� �ð�
	private int third_thread_Time; // ����° Ȱ�ַ� �����忡���� �ð�����
	
	private JTextArea dep_sp3;
	
	SharedMemory ShareAirplane = new SharedMemory();
	
	public Runway_3(JTextArea dep_sp3)
	{
		this.dep_sp3=dep_sp3;
		//ť ����
		dep_eland_Q=new Vector<Airplane>();
		//�ε��� �ʱ�ȭ
		q_ind=0;
		//ī��Ʈ �ʱ�ȭ
		dep_count=emer_count=real_depcount=0;
		//��� �ð� �ʱ�ȭ
		dep_waitAver=emer_wtime=0;
		third_thread_Time=0;
	}
	
	public void run()
	{
		/*3��Ȱ�ַ� �������Դϴ�*/
		if(Thread.currentThread() == Depart_Land.third_th)
		{
			third_thread_Time=0;
			while(third_thread_Time < Depart_Land.PlayTime-1)
			{
				if ((third_thread_Time!=0&&third_thread_Time%50==0)||third_thread_Time==0)
				{
					dep_sp3.setText("");
				}
				String old=dep_sp3.getText();
				dep_sp3.setText("");
				dep_sp3.append(third_thread_Time+"��: \n");
				dep_sp3.append(old);
				
				if(ShareAirplane.GetisReady() == true) // ���� ���� Ŭ�������� ��� ���� ����Ⱑ �ִٰ� ��ȣ�� �´ٸ�
					ShareAirplane.GetEmrAirplane();
				
				if(dep_eland_Q.size()!=0 && (q_ind < dep_eland_Q.size()) )
					Depart_Land.thirdRunway.Dequeue(third_thread_Time);
				try
				{
					Thread.sleep(1000/Depart_Land.Speed);
				}
				catch(InterruptedException e1) {return;}
				catch(IllegalArgumentException e2) {return;}
				third_thread_Time++;
			}
			
			String old=dep_sp3.getText();
			dep_sp3.setText("");
			dep_sp3.append(third_thread_Time+"�� :\n");
			dep_sp3.append(old);
		}
	}
	//****************************************
	//Enqueue ��Ȳ ó��(�⺻)1
	public void Enqueue(int present_time, int count)
	{
		for (int i=0; i<count; i++)
			dep_eland_Q.add(new Airplane(present_time,1));//����� �ϳ� �� �ڿ� Enqueue
		dep_count+=count;//�̷� ����� ī��Ʈ ������Ű��
		q_enq_status=count+"���� ����Ⱑ �̷� ��û\n";
		String old=Depart_Land.thirdRunway.dep_sp3.getText();
		Depart_Land.thirdRunway.dep_sp3.setText("");
		Depart_Land.thirdRunway.dep_sp3.append(q_enq_status);
		Depart_Land.thirdRunway.dep_sp3.append(old);
	}
	//��� ���� ����� Enqueue ��Ȳ ó��
	//��� ������ �ּ� 10�� ���Ŀ� �߻��ϴ�
	//ť�� ��� �ִ��� �˻��� �ʿ� ����
	public void Emer_Land_Enqueue(Airplane Emergency)
	{
		dep_eland_Q.add(q_ind, Emergency);//���� �̷��� ����� �տ��ٰ� ��� ����� ���� ��
		q_emer_enq_status="��� ���� ����Ⱑ �̷� Ȱ�ַη� ����\n";//�޽��� ó��
		String old=Depart_Land.thirdRunway.dep_sp3.getText();
		Depart_Land.thirdRunway.dep_sp3.setText("");
		Depart_Land.thirdRunway.dep_sp3.append(q_emer_enq_status);
		Depart_Land.thirdRunway.dep_sp3.append(old);
		
	}
	//*****************************************
	//Dequeue ��Ȳ ó��
	public void Dequeue(int present_time)
	{
		if (dep_eland_Q.size()!=0)
		{
			dep_eland_Q.get(q_ind).SetDeQ(present_time);//��� �ð� �� �̷�(��� ����) �ð� ����
			if (dep_eland_Q.get(q_ind).isEmergency==true)
			{//��� ���� ������̸�
				emer_wtime+=dep_eland_Q.get(q_ind).GetWTime();//��� ���� ������� ��� ���ð� �ջ�
				q_deq_status=dep_eland_Q.get(q_ind).GetNumber()+" ����Ⱑ ��� ������\n";
				emer_count++;//��� ���� ����� ī��Ʈ ������Ű��
				q_ind++;//�޽��� ó�� �� ���� ó�� ������ �Ѿ
			}
			else
			{//�Ϲ� �̷� ������̸�
				dep_waitAver+=dep_eland_Q.get(q_ind).GetWTime();//��� ��� �ð� �߰�
				q_deq_status=dep_eland_Q.get(q_ind).GetNumber()+" ����Ⱑ �̷���\n";
				real_depcount++;//�̷��� ����� �� ����
				q_ind++;//���� ó�� ������ �Ѿ
			}
			
			String old=Depart_Land.thirdRunway.dep_sp3.getText();
			Depart_Land.thirdRunway.dep_sp3.setText("");
			Depart_Land.thirdRunway.dep_sp3.append(q_deq_status);
			Depart_Land.thirdRunway.dep_sp3.append(old);
		}
	}
	//*****************************************
	//ť ������ ����
	public int GetQSize()
	{
		if (dep_eland_Q.size()!=0)
			return dep_eland_Q.size();
		else
			return 0;
	}
	//*****************************************
	//��� �ð� ����
	public int GetDepWtime()
	{
		return dep_waitAver;
	}
	public int GetRealDepCount()
	{
		return real_depcount;
	}
	//*****************************************
	//��� ������ �������� ��� ���ð� ��ȯ
	public int GetEmerLandWtime()
	{
		return emer_wtime;
	}
	//*****************************************
	//���� ī��Ʈ ����
	public int GetDepCount()
	{
		return dep_count;
	}
	public int GetEmergencyCount()
	{
		return emer_count;
	}
	public int Get3rdThreadTime()
	{
		return third_thread_Time;
	}

	public int Get3rdIndex() {
		// TODO �ڵ� ������ �޼ҵ� ����
		return q_ind;
	}
	public int NotDequeueCount()
   	{
		return dep_eland_Q.size()-q_ind;
   	}
}