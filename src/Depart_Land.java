package n_dep_n_land;
import javax.swing.*;
public class Depart_Land implements Runnable {
   static int StartTime=0;    // ���� �ð��� ī������ ��������
   static int PlayTime=1440;  // �ð��Է� ���� �ѽð�(����Ʈ 1440)
   static int Speed =1;       // ����ڰ� ���� ��ư�� ������ ���� ���ǵ� ����(����Ʈ 1)
   static int percentage=100;

   static Runway_12 firstRunway;
   static Runway_12 secondRunway;
   static Runway_3 thirdRunway;

   static Thread first_th;
   static Thread second_th;
   static Thread third_th;

   private int l_random_count;   // �� ���� �ð�����  ���� ����� ����
   private int d_random_count;   // �� ���� �ð�����  �̷� ����� ����
   private JButton start;
   private JTextArea progress;
   

   PrintStat stat;
   
   SharedMemory share = new SharedMemory();
   public Depart_Land(JTextArea dep_ta1, JTextArea land_ta1, JTextArea land_ta2, JTextArea dep_ta2,
            JTextArea land_ta3, JTextArea land_ta4, JTextArea dep_sp3, JTextArea progress, JButton start)
   {
      firstRunway=new Runway_12(dep_ta1,land_ta1,land_ta2);
      secondRunway = new Runway_12(dep_ta2,land_ta3,land_ta4);
      thirdRunway = new Runway_3(dep_sp3);
      
      first_th = new Thread(firstRunway);
      second_th = new Thread(secondRunway);// ������ ����
      third_th=new Thread(thirdRunway);

      this.start=start;
      this.progress=progress;
      
      l_random_count = 0;   
      d_random_count = 0;   
      stat=new PrintStat(firstRunway,secondRunway, thirdRunway);
   }
   // ���� �ð����� ��/���� ����� ���� ���� ���� ������
   public void SetCountAirPlane()
   {
      int dep_value;//�̷� ������ �����ϴ� �����
      int land_value;//���� ������ �����ϴ� �����

      dep_value=(int)(Math.random()*100)+1;
      land_value=(int)(Math.random()*100)+1;
      //30%�� Ȯ���� 1~3���� ����� ����-> 1~3���� ����Ⱑ ���� Ȯ���� 30%
      if (dep_value<percentage)
         d_random_count=(int)(Math.random()*3)+1;
      else
         d_random_count=0;
      if (land_value<percentage)
         l_random_count=(int)(Math.random()*3)+1;
      else
         l_random_count=0;

   }
   public void run()
   {
      StartTime=0;
      
      while(StartTime<PlayTime)
      {
         //�����尴ü�� ������ �Ǿ����� ���� ���� ���� ���� ���¶��  START !!
         if(first_th.getState() == Thread.State.NEW)
         {
            firstRunway.ShareAirplane= share; // ����Ŭ������ ���� ��Ų��.
            first_th.start();   // 1��Ȱ�ַ� ����
         }
         if(second_th.getState() == Thread.State.NEW)
         {
            secondRunway.ShareAirplane = share; // ���� Ŭ������ �����Ų��.
            second_th.start(); // 2�� Ȱ�ַ� ����
         }

         if(third_th.getState() == Thread.State.NEW)
         {
            thirdRunway.ShareAirplane = share; // ���� Ŭ������ ���� ��Ų��.
            third_th.start(); // 3�� Ȱ�ַ� ����
         }
 
         this.SetCountAirPlane(); // ����� ���� ���� ����
         
         if (StartTime!=0 && StartTime%10==0)
         {
            String middle=stat.MiddleStat();
            progress.append(middle);
         }

         if(d_random_count != 0) // �̷��� ������� ���� �� 0�� �ƴϸ�
         {
            int first=firstRunway.SizeofDefQ();
            int second=secondRunway.SizeofDefQ();
            int third=thirdRunway.GetQSize();
            int min=first;
            if (min>second)
               min=second;
            if (min>third)
               min=third;

            if(min==first)
               min=1;
            else if (min==second)
               min=2;
            else if (min==third)
               min=3;
               switch(min)
               {
               case 1:
                  firstRunway.Dep_Enqueue(firstRunway.Get1stThreadTime(),d_random_count, 1);
                  break;
               case 2:
                  secondRunway.Dep_Enqueue(secondRunway.Get2ndThreadTime(),d_random_count, 2);
                  break;
               case 3:
                  thirdRunway.Enqueue(thirdRunway.Get3rdThreadTime(),d_random_count);
                  break;
               }
         }
         if(l_random_count != 0)  // ������ ������� ������ 0�� �ƴϸ�
         {
            int min=0;
            int fir_land=firstRunway.LandQSizeaver();
            int sec_land=secondRunway.LandQSizeaver();
            if (fir_land>sec_land)
            {
               if (secondRunway.SizeofLandQ_1()<secondRunway.SizeofLandQ_2())
                  min=3;
               else
                  min=4;
            }
            else
            {
               if (firstRunway.SizeofLandQ_1()<firstRunway.SizeofLandQ_2())
                  min=1;
               else
                  min=2;
            }

            switch(min)
            {
            case 1:
               firstRunway.Land1_Enqueue(firstRunway.Get1stThreadTime(),l_random_count,1);
               break;
            case 2:
               firstRunway.Land2_Enqueue(firstRunway.Get1stThreadTime(),l_random_count,2);
               break;
            case 3:
               secondRunway.Land1_Enqueue(secondRunway.Get2ndThreadTime(),l_random_count,3);
               break;
            case 4:
               secondRunway.Land2_Enqueue(secondRunway.Get2ndThreadTime(),l_random_count,4);
               break;
            }
         }
         try
         {
            Thread.sleep(1000/Depart_Land.Speed); // ����ڰ� �Է����� speed �� ��ŭ ����� ������Ų��.
         }
         catch(InterruptedException e)
         {
            //sleep�� ������ �߻��ϸ� interrupt
            first_th.interrupt();
            second_th.interrupt();
            third_th.interrupt();
            return;
         }
         StartTime++; // ���� �ð� ��������~
      }
      if (StartTime==PlayTime)
      {
         JOptionPane.showMessageDialog(null, stat.FinalStat(),"���� ���", JOptionPane.INFORMATION_MESSAGE);
         start.setEnabled(true);
         Thread.interrupted();
      }
   }
}