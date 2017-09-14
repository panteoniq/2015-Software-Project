package n_dep_n_land;

public class Airplane {
	private int plane_num;//���� �ĺ� ��ȣ
	private int enq_time;//Enqueue�� �ð�
	private int deq_time;//Dequeue�� �ð�
	private int wait_time;//��� �ð�
	private int fuel;//���ᷮ. �̷� ������� ��� -1 �Ҵ�
	private int remaintime;//���� ���� �ð�. �̷� ������� ��� ��û ū ���� �ο�
	private int priority;//�켱 ����. �Ϲ� ������ ������� ��� 2�� �켱 ���� �ο�
	//��� ������ �õ��ؾ� �ϴ� ������ 1�� �켱 ���� �Ҵ�
	boolean isdeq;//�̷�,�����Ͽ��°�?
	boolean isEmergency;//��� ���� ������ΰ�?
	static boolean[] pnum_ary=new boolean[1000000];//static ������ �����ؾ� �ߺ� ���� ��ȣ���� Ȯ�� ����
	public Airplane(int time, int selector)//���� �ð��� ����� ������ �Ű� ������ ���޹���
	{
		//selector -> 1�̸� �̷�, 2�̸� ����
		//���� ��ȣ �Ҵ�
		if (selector==1)//�̷� �������
		{
			while(true)
			{
				plane_num=(int)(Math.random()*999000)+100;//100~999999������ ���� �߻�
				if ((plane_num%2)==0)  //¦�����
					plane_num+=1;      //1 ���ؼ� Ȧ���� ����
				if (plane_num==1000000)//���� ��ȣ�� 1000000�� ���
					plane_num-=2;//2�� ���ش�(100~999998)
				if (pnum_ary[plane_num]==true) //���� ���� �� �ִ� ���� ��ȣ���
					continue;//������ϱ� ���� continue
				else//���� �� ���ٸ�
				{
					pnum_ary[plane_num]=true;//�ش� ��ȣ�� ���Ǿ������� �ٲٰ�
					break;//���� Ż��
				}
			}
			fuel=100;//���ᷮ�� 100�� ����(�̷� ��������� ǥ��)
			remaintime=100000000;//���� ���� �ð��� ��û �������� ����
			priority=2;//�켱 ���� ����
		}
		else//���� �������
		{
			while(true)
			{
				plane_num=(int)(Math.random()*999000)+100;//100~999999������ ���� �߻�
				if ((plane_num%2)==1)   //Ȧ�����
					plane_num+=1;       //1 ���ؼ� ¦���� ����(101~999999)
				if (pnum_ary[plane_num]==true) //���� ���� �� �ִ� ���� ��ȣ���
					continue;//������ϱ� ���� continue
				else//���� �� ���ٸ�
				{
					pnum_ary[plane_num]=true;//�ش� ��ȣ�� ���Ǿ������� �ٲٰ�
					break;//���� Ż��
				}
			}
			fuel=(int)(Math.random()*61)+20; //20~80 ������ ���� ����
			remaintime=fuel/2;
			priority=2;//�켱 ���� ����
		}
		//Enqueue Time ����
		enq_time=time;
		//Dequeue Time �ʱ�ȭ
		deq_time=0;
		//Wait Time �ʱ�ȭ
		wait_time=0;
		//��,�������� �ʾ������� �ʱ� ������ false��
		isdeq=false;
		isEmergency=false;
	}
	//��� ���� ����⿡ ������ ������ �� ���� Setter*******************
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
	public void ReduceFuel()//���ᷮ ����
	{
		fuel-=2;
	}
	public void OperationRemainTime()
	{//���� ���� �ð� ����
		remaintime=fuel/2;
	}
	public void SetDeQ(int present_time)
	{//�̷� �ð��� ��� �ð��� ����ϱ� ���� ���� �ð��� �Ű� ������ ����
		deq_time=present_time;
		wait_time=deq_time-enq_time;
		isdeq=true;
	}
	public void SetEmerPriority()//�켱 ������ 1�κ���
	{
		priority=1;
	}
	public void SetEmergency()//��� ������ �ʿ��� ������ ����
	{
		isEmergency=true;
	}
	public int GetRemainTime()//���� ���� �ð� ��ȯ
	{
		return remaintime;
	}
	public int GetFuel()//���ᷮ ��ȯ
	{
		return fuel;
	}
	public int GetWTime()//��� �ð� ��ȯ
	{
		return wait_time;
	}
	public int GetNumber()//����� ���� ��ȣ ��ȯ
	{
		return plane_num;
	}
	public boolean GetEmergencyStatus()
	{
		return isEmergency;
	}
}