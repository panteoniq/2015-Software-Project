package n_dep_n_land;

public class Airplane {
	private int plane_num;//고유 식별 번호
	private int enq_time;//Enqueue된 시간
	private int deq_time;//Dequeue된 시간
	private int wait_time;//대기 시간
	private int fuel;//연료량. 이륙 비행기일 경우 -1 할당
	private int remaintime;//비행 가능 시간. 이륙 비행기일 경우 엄청 큰 수로 부여
	private int priority;//우선 순위. 일반 이착륙 비행기일 경우 2의 우선 순위 부여
	//긴급 착륙을 시도해야 하는 비행기는 1의 우선 순위 할당
	boolean isdeq;//이륙,착륙하였는가?
	boolean isEmergency;//긴급 착륙 비행기인가?
	static boolean[] pnum_ary=new boolean[1000000];//static 변수로 선언해야 중복 고유 번호인지 확인 가능
	public Airplane(int time, int selector)//현재 시간과 비행기 종류를 매개 변수로 전달받음
	{
		//selector -> 1이면 이륙, 2이면 착륙
		//고유 번호 할당
		if (selector==1)//이륙 비행기라면
		{
			while(true)
			{
				plane_num=(int)(Math.random()*999000)+100;//100~999999까지의 난수 발생
				if ((plane_num%2)==0)  //짝수라면
					plane_num+=1;      //1 더해서 홀수로 만듦
				if (plane_num==1000000)//고유 번호가 1000000일 경우
					plane_num-=2;//2를 빼준다(100~999998)
				if (pnum_ary[plane_num]==true) //만약 사용된 적 있는 고유 번호라면
					continue;//재생성하기 위해 continue
				else//사용된 적 없다면
				{
					pnum_ary[plane_num]=true;//해당 번호를 사용되었음으로 바꾸고
					break;//루프 탈출
				}
			}
			fuel=100;//연료량은 100로 설정(이륙 비행기임을 표시)
			remaintime=100000000;//비행 가능 시간은 엄청 많음으로 설정
			priority=2;//우선 순위 설정
		}
		else//착륙 비행기라면
		{
			while(true)
			{
				plane_num=(int)(Math.random()*999000)+100;//100~999999까지의 난수 발생
				if ((plane_num%2)==1)   //홀수라면
					plane_num+=1;       //1 더해서 짝수로 만듦(101~999999)
				if (pnum_ary[plane_num]==true) //만약 사용된 적 있는 고유 번호라면
					continue;//재생성하기 위해 continue
				else//사용된 적 없다면
				{
					pnum_ary[plane_num]=true;//해당 번호를 사용되었음으로 바꾸고
					break;//루프 탈출
				}
			}
			fuel=(int)(Math.random()*61)+20; //20~80 사이의 연료 생성
			remaintime=fuel/2;
			priority=2;//우선 순위 설정
		}
		//Enqueue Time 설정
		enq_time=time;
		//Dequeue Time 초기화
		deq_time=0;
		//Wait Time 초기화
		wait_time=0;
		//이,착륙되지 않았음으로 초기 설정은 false로
		isdeq=false;
		isEmergency=false;
	}
	//긴급 착륙 비행기에 정보를 저장할 때 쓰는 Setter*******************
	public void SetPlaneNum(int num)
	{
		plane_num=num;
	}
	public void SetEnQTime(int time)
	{
		enq_time=time;
	}
	public void SetFuel(int fuel)
	{
		this.fuel=fuel;
	}
	public int GetEnQTime()
	{
		return enq_time;
	}
	//*********************************************************
	public void ReduceFuel()//연료량 감소
	{
		fuel-=2;
	}
	public void OperationRemainTime()
	{//비행 가능 시간 갱신
		remaintime=fuel/2;
	}
	public void SetDeQ(int present_time)
	{//이륙 시간과 대기 시간을 계산하기 위해 현재 시간을 매개 변수로 받음
		deq_time=present_time;
		wait_time=deq_time-enq_time;
		isdeq=true;
	}
	public void SetEmerPriority()//우선 순위를 1로변경
	{
		priority=1;
	}
	public void SetEmergency()//긴급 착륙이 필요한 비행기로 변경
	{
		isEmergency=true;
	}
	public int GetRemainTime()//비행 가능 시간 반환
	{
		return remaintime;
	}
	public int GetFuel()//연료량 반환
	{
		return fuel;
	}
	public int GetWTime()//대기 시간 반환
	{
		return wait_time;
	}
	public int GetNumber()//비행기 고유 번호 반환
	{
		return plane_num;
	}
	public boolean GetEmergencyStatus()
	{
		return isEmergency;
	}
}