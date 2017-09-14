package n_dep_n_land;

import java.util.Vector;

import javax.swing.JTextArea;
public class Runway_3 implements Runnable {
	private Vector<Airplane> dep_eland_Q;//이륙 및 긴급 착륙 큐
	private int q_ind;//해당 큐에 대한 인덱스

	private String q_enq_status="";//Enqueue 메시지
	private String q_emer_enq_status="";//긴급 착륙 비행기 Enqueue 메시지
	private String q_deq_status="";//Dequeue 메시지

	private int dep_count;
	private int real_depcount;//진짜 이륙한 비행기 수
	private int emer_count;//긴급 착륙과 이륙이 한 큐에서 이루어지기 때문에
	//숫자를 나누어서 셈
	private int dep_waitAver;//이륙 비행기의 평균 대기 시간
	private int emer_wtime;//긴급 착륙 비행기의 평균 대기 시간
	private int third_thread_Time; // 세번째 활주로 스레드에서의 시간변수
	
	private JTextArea dep_sp3;
	
	SharedMemory ShareAirplane = new SharedMemory();
	
	public Runway_3(JTextArea dep_sp3)
	{
		this.dep_sp3=dep_sp3;
		//큐 생성
		dep_eland_Q=new Vector<Airplane>();
		//인덱스 초기화
		q_ind=0;
		//카운트 초기화
		dep_count=emer_count=real_depcount=0;
		//평균 시간 초기화
		dep_waitAver=emer_wtime=0;
		third_thread_Time=0;
	}
	
	public void run()
	{
		/*3번활주로 스레드입니다*/
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
				dep_sp3.append(third_thread_Time+"분: \n");
				dep_sp3.append(old);
				
				if(ShareAirplane.GetisReady() == true) // 만일 공유 클래스에서 긴급 착륙 비행기가 있다고 신호가 온다면
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
			dep_sp3.append(third_thread_Time+"분 :\n");
			dep_sp3.append(old);
		}
	}
	//****************************************
	//Enqueue 상황 처리(기본)1
	public void Enqueue(int present_time, int count)
	{
		for (int i=0; i<count; i++)
			dep_eland_Q.add(new Airplane(present_time,1));//비행기 하나 맨 뒤에 Enqueue
		dep_count+=count;//이륙 비행기 카운트 증가시키고
		q_enq_status=count+"대의 비행기가 이륙 요청\n";
		String old=Depart_Land.thirdRunway.dep_sp3.getText();
		Depart_Land.thirdRunway.dep_sp3.setText("");
		Depart_Land.thirdRunway.dep_sp3.append(q_enq_status);
		Depart_Land.thirdRunway.dep_sp3.append(old);
	}
	//긴급 착륙 비행기 Enqueue 상황 처리
	//긴급 착륙은 최소 10분 이후에 발생하니
	//큐가 비어 있는지 검사할 필요 없음
	public void Emer_Land_Enqueue(Airplane Emergency)
	{
		dep_eland_Q.add(q_ind, Emergency);//현재 이륙할 비행기 앞에다가 긴급 비행기 넣은 후
		q_emer_enq_status="긴급 착륙 비행기가 이륙 활주로로 들어옴\n";//메시지 처리
		String old=Depart_Land.thirdRunway.dep_sp3.getText();
		Depart_Land.thirdRunway.dep_sp3.setText("");
		Depart_Land.thirdRunway.dep_sp3.append(q_emer_enq_status);
		Depart_Land.thirdRunway.dep_sp3.append(old);
		
	}
	//*****************************************
	//Dequeue 상황 처리
	public void Dequeue(int present_time)
	{
		if (dep_eland_Q.size()!=0)
		{
			dep_eland_Q.get(q_ind).SetDeQ(present_time);//대기 시간 및 이륙(긴급 착륙) 시간 설정
			if (dep_eland_Q.get(q_ind).isEmergency==true)
			{//긴급 착륙 비행기이면
				emer_wtime+=dep_eland_Q.get(q_ind).GetWTime();//긴급 착륙 비행기의 평균 대기시간 합산
				q_deq_status=dep_eland_Q.get(q_ind).GetNumber()+" 비행기가 긴급 착륙함\n";
				emer_count++;//긴급 착륙 비행기 카운트 증가시키고
				q_ind++;//메시지 처리 후 다음 처리 비행기로 넘어감
			}
			else
			{//일반 이륙 비행기이면
				dep_waitAver+=dep_eland_Q.get(q_ind).GetWTime();//평균 대기 시간 추가
				q_deq_status=dep_eland_Q.get(q_ind).GetNumber()+" 비행기가 이륙함\n";
				real_depcount++;//이륙한 비행기 수 증가
				q_ind++;//다음 처리 비행기로 넘어감
			}
			
			String old=Depart_Land.thirdRunway.dep_sp3.getText();
			Depart_Land.thirdRunway.dep_sp3.setText("");
			Depart_Land.thirdRunway.dep_sp3.append(q_deq_status);
			Depart_Land.thirdRunway.dep_sp3.append(old);
		}
	}
	//*****************************************
	//큐 사이즈 리턴
	public int GetQSize()
	{
		if (dep_eland_Q.size()!=0)
			return dep_eland_Q.size();
		else
			return 0;
	}
	//*****************************************
	//평균 시간 리턴
	public int GetDepWtime()
	{
		return dep_waitAver;
	}
	public int GetRealDepCount()
	{
		return real_depcount;
	}
	//*****************************************
	//긴급 착륙한 비행기들의 평균 대기시간 반환
	public int GetEmerLandWtime()
	{
		return emer_wtime;
	}
	//*****************************************
	//각종 카운트 리턴
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
		// TODO 자동 생성된 메소드 스텁
		return q_ind;
	}
	public int NotDequeueCount()
   	{
		return dep_eland_Q.size()-q_ind;
   	}
}