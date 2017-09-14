package n_dep_n_land;
import javax.swing.*;
public class Depart_Land implements Runnable {
   static int StartTime=0;    // 시작 시간을 카운터할 전역변수
   static int PlayTime=1440;  // 시간입력 받은 총시간(디폴트 1440)
   static int Speed =1;       // 사용자가 시작 버튼을 눌렀을 때의 스피드 변수(디폴트 1)
   static int percentage=100;

   static Runway_12 firstRunway;
   static Runway_12 secondRunway;
   static Runway_3 thirdRunway;

   static Thread first_th;
   static Thread second_th;
   static Thread third_th;

   private int l_random_count;   // 매 단위 시간마다  착륙 비행기 개수
   private int d_random_count;   // 매 단위 시간마다  이륙 비행기 개수
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
      second_th = new Thread(secondRunway);// 스레드 생성
      third_th=new Thread(thirdRunway);

      this.start=start;
      this.progress=progress;
      
      l_random_count = 0;   
      d_random_count = 0;   
      stat=new PrintStat(firstRunway,secondRunway, thirdRunway);
   }
   // 단위 시간마다 이/착륙 비행기 개수 랜덤 으로 지정함
   public void SetCountAirPlane()
   {
      int dep_value;//이륙 난수를 생성하는 비행기
      int land_value;//착륙 난수를 생성하는 비행기

      dep_value=(int)(Math.random()*100)+1;
      land_value=(int)(Math.random()*100)+1;
      //30%의 확률로 1~3대의 비행기 생성-> 1~3대의 비행기가 들어올 확률은 30%
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
         //스레드객체가 생성은 되었지만 아직 실행 되지 않은 상태라면  START !!
         if(first_th.getState() == Thread.State.NEW)
         {
            firstRunway.ShareAirplane= share; // 공유클래스와 연결 시킨다.
            first_th.start();   // 1번활주로 시작
         }
         if(second_th.getState() == Thread.State.NEW)
         {
            secondRunway.ShareAirplane = share; // 공유 클래스와 연결시킨다.
            second_th.start(); // 2번 활주로 시작
         }

         if(third_th.getState() == Thread.State.NEW)
         {
            thirdRunway.ShareAirplane = share; // 공유 클래스와 연결 시킨다.
            third_th.start(); // 3번 활주로 시작
         }
 
         this.SetCountAirPlane(); // 비행기 랜덤 개수 지정
         
         if (StartTime!=0 && StartTime%10==0)
         {
            String middle=stat.MiddleStat();
            progress.append(middle);
         }

         if(d_random_count != 0) // 이륙한 비행기의 개수 가 0이 아니면
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
         if(l_random_count != 0)  // 착륙한 비행기의 개수가 0이 아니면
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
            Thread.sleep(1000/Depart_Land.Speed); // 사용자가 입력해준 speed 값 만큼 배속을 증가시킨다.
         }
         catch(InterruptedException e)
         {
            //sleep에 문제가 발생하면 interrupt
            first_th.interrupt();
            second_th.interrupt();
            third_th.interrupt();
            return;
         }
         StartTime++; // 수행 시간 증가증가~
      }
      if (StartTime==PlayTime)
      {
         JOptionPane.showMessageDialog(null, stat.FinalStat(),"최종 결과", JOptionPane.INFORMATION_MESSAGE);
         start.setEnabled(true);
         Thread.interrupted();
      }
   }
}