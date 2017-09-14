package n_dep_n_land;
// �����尡�� ���� ���� Ŭ����
public class SharedMemory {
   private Airplane emerAirplane[][] = new Airplane[2][2]; // 2,2 ��ķ� �ʱ�ȭ 
   private boolean isReady[][] = new boolean[2][2];
   private int emr_count; // ��������� �䱸�� ����� ����
   private int true_count;// true�� ������ Ȯ���ϴ� ����

   public SharedMemory()
   {
      for(int i=0;i<emerAirplane.length;i++)
         for(int j =0;j<emerAirplane[i].length;j++)
            emerAirplane[i][j] = null;

      for(int i=0;i<isReady.length;i++)
         for(int j=0;j<isReady[i].length;j++)
            isReady[i][j] = false;
      emr_count = 0;
      true_count = 0; // 
   }
   public synchronized void SetEmrAirplane(Airplane air, double position)//��� ���� ����⸦ ���� �޸𸮷� �����ϴ� �Լ�
   {
      String str = Double.toString(position); // �������� ���������� �ش�.

      switch(str)
      {
      case "1.1":
         emerAirplane[0][0] = air;
         isReady[0][0] = true;
         break;
      case "1.2":
         emerAirplane[1][0] = air;
         isReady[1][0] = true;
         break;
      case "2.1":
         emerAirplane[0][1] = air;
         isReady[0][1] = true;
         break;
      case "2.2":
         emerAirplane[1][1] = air;
         isReady[1][1] = true;
         break;
      }
      emr_count++;

   }
   public synchronized void GetEmrAirplane()//��� ���� ����⸦ �й��ϴ� �Լ�
   {

      if(true_count == 1)//��� ������ ������� ī��Ʈ�� 1�̶�� 
      {//������ 3������ ���Բ�
         for(int i=0;i<isReady.length;i++) {
            for(int j=0;j<isReady[i].length;j++)
            {
               if(isReady[i][j] == true) // ��������� ǥ�� �Ǿ��ִٸ�
               {
            	  // ����° Ȱ�ַη� ������� ��Ų��.
                  Depart_Land.thirdRunway.Emer_Land_Enqueue(emerAirplane[i][j]);
                  emerAirplane[i][j]=null; // �׸��� ���� �� ����⸦ null �� �ʱ�ȭ��Ų��.
                  isReady[i][j]=false;     // üũ ���� ���� false�� �ʱ�ȭ ��Ų��.
                  true_count = 0;          // ������� �������� 0���� �ʱ�ȭ ��Ų��.
               }
            }
         }
      }

      else
      {//�� ���� ���
         boolean third=false;
         for(int i=0;i<isReady.length;i++)
         {//���� ó������ �� ���� 3������ �־���� �ϴµ�
            for(int j=0;j<isReady[i].length;j++)
            {//for���̴� ���� �̶� true�� ��� ��ҵ��� ����⸦ �� �־��ְ� �ǹǷ�
               if(isReady[i][j] == true)
               {
                  Depart_Land.thirdRunway.Emer_Land_Enqueue(emerAirplane[i][j]);
                  emerAirplane[i][j]=null;
                  isReady[i][j]=false;
                  true_count--;
                  third=true;//�� ���� �̷���ų �� �ֵ��� ������ ����
                  break;
               }
            }
            if (third)
               break;
         }
         if (true_count!=3)
         {
            //�� ���Ŀ��� ���� �Դ� �ڸ��� ��������
            for(int i=0;i<isReady.length;i++)
            {
               for(int j=0;j<isReady[i].length;j++)
               {
                  if(isReady[i][j] == true)
                  {
                     if (i==0&&j==0) //1�� Ȱ�ַ� - 1�� ���� ť
                     {
                        Depart_Land.firstRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl1EmerEnqueue();
                     }
                     else if (i==1&&j==0) //1�� Ȱ�ַ�-2�� ���� ť
                     {
                        Depart_Land.firstRunway.Emer_Land2_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl2EmerEnqueue();
                     }
                     else if (i==0&&j==1)//2�� Ȱ�ַ� - 1�� ���� ť
                     {
                        Depart_Land.secondRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.secondRunway.Setl1EmerEnqueue();
                     }
                     else//2�� Ȱ�ַ�-2�� ���� ť
                     {
                        Depart_Land.secondRunway.Emer_Land2_Enqueue(emerAirplane[i][j]);
                        Depart_Land.secondRunway.Setl2EmerEnqueue();
                     }
                     emerAirplane[i][j]=null;
                     isReady[i][j]=false;
                     true_count = 0;
                  }
               }
            }
         }
         else
         {
            emerAirplane[1][1]=null;
            isReady[1][1]=false;
            //2�� Ȱ�ַ��� 2�� ���� ť�� ���ؼ� ��� ī��Ʈ 1 ����
            Depart_Land.secondRunway.IncrL2AccCount();
            
            for(int i=0;i<isReady.length;i++)
            {
               for(int j=0;j<isReady[i].length;j++)
               {
                  if(isReady[i][j] == true)
                  {
                     if (i==0&&j==0) //1�� Ȱ�ַ� - 1�� ���� ť
                     {
                        Depart_Land.firstRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl1EmerEnqueue();
                     }
                     else if (i==0&&j==1) //1�� Ȱ�ַ�-2�� ���� ť
                     {
                        Depart_Land.firstRunway.Emer_Land2_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl2EmerEnqueue();
                     }
                     else if (i==1&&j==0)//2�� Ȱ�ַ� - 1�� ���� ť
                     {
                        Depart_Land.secondRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.secondRunway.Setl1EmerEnqueue();
                     }
                     emerAirplane[i][j]=null;
                     isReady[i][j]=false;
                     true_count = 0;
                  }
               }
            }
         }
      }
   }
   //��� ���� ����Ⱑ �ִ��� ������ 3�� Ȱ�ַο��� Ȯ���ϴ� �Լ�
   public boolean GetisReady() 
   {
      for(int i=0;i<isReady.length;i++)       
         for(int j=0;j<isReady[i].length;j++)
         {
            if(isReady[i][j] == true) // ��������� �߻� �Ͽ��ٸ�
            {
               true_count++;          // ��������� ������ 1 ���� ��Ų��.
            }
         }
      if(true_count != 0)
         return true; // ��� ���� ����Ⱑ �ֽ��ϴ�.
      else
         return false;// ��� ���� ����Ⱑ �����ϴ�.
   }
   public int GetEmerCount()//��� ������ ������� ���� ��ȯ
   {
      return emr_count;
   }
}