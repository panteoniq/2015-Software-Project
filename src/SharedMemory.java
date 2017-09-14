package n_dep_n_land;
// 스레드가의 공유 영역 클래스
public class SharedMemory {
   private Airplane emerAirplane[][] = new Airplane[2][2]; // 2,2 행렬로 초기화 
   private boolean isReady[][] = new boolean[2][2];
   private int emr_count; // 긴급착륙을 요구한 비행기 개수
   private int true_count;// true의 개수를 확인하는 변수

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
   public synchronized void SetEmrAirplane(Airplane air, double position)//긴급 착륙 비행기를 공유 메모리로 전달하는 함수
   {
      String str = Double.toString(position); // 더블형을 문자형으로 준다.

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
   public synchronized void GetEmrAirplane()//긴급 착륙 비행기를 분배하는 함수
   {

      if(true_count == 1)//긴급 착륙한 비행기의 카운트가 1이라면 
      {//무조건 3번으로 가게끔
         for(int i=0;i<isReady.length;i++) {
            for(int j=0;j<isReady[i].length;j++)
            {
               if(isReady[i][j] == true) // 긴급착륙이 표시 되어있다면
               {
            	  // 세번째 활주로로 긴급착륙 시킨다.
                  Depart_Land.thirdRunway.Emer_Land_Enqueue(emerAirplane[i][j]);
                  emerAirplane[i][j]=null; // 그리고 현재 그 비행기를 null 로 초기화시킨다.
                  isReady[i][j]=false;     // 체크 변수 또한 false로 초기화 시킨다.
                  true_count = 0;          // 긴급착륙 비행기수도 0으로 초기화 시킨다.
               }
            }
         }
      }

      else
      {//그 외의 경우
         boolean third=false;
         for(int i=0;i<isReady.length;i++)
         {//제일 처음에는 한 놈을 3번에다 넣어줘야 하는데
            for(int j=0;j<isReady[i].length;j++)
            {//for문이다 보니 이때 true인 모든 요소들의 비행기를 다 넣어주게 되므로
               if(isReady[i][j] == true)
               {
                  Depart_Land.thirdRunway.Emer_Land_Enqueue(emerAirplane[i][j]);
                  emerAirplane[i][j]=null;
                  isReady[i][j]=false;
                  true_count--;
                  third=true;//한 번만 이륙시킬 수 있도록 변수를 설정
                  break;
               }
            }
            if (third)
               break;
         }
         if (true_count!=3)
         {
            //그 이후에는 원래 왔던 자리로 돌려보냄
            for(int i=0;i<isReady.length;i++)
            {
               for(int j=0;j<isReady[i].length;j++)
               {
                  if(isReady[i][j] == true)
                  {
                     if (i==0&&j==0) //1번 활주로 - 1번 착륙 큐
                     {
                        Depart_Land.firstRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl1EmerEnqueue();
                     }
                     else if (i==1&&j==0) //1번 활주로-2번 착륙 큐
                     {
                        Depart_Land.firstRunway.Emer_Land2_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl2EmerEnqueue();
                     }
                     else if (i==0&&j==1)//2번 활주로 - 1번 착륙 큐
                     {
                        Depart_Land.secondRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.secondRunway.Setl1EmerEnqueue();
                     }
                     else//2번 활주로-2번 착륙 큐
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
            //2번 활주로의 2번 착륙 큐에 대해서 사고 카운트 1 증가
            Depart_Land.secondRunway.IncrL2AccCount();
            
            for(int i=0;i<isReady.length;i++)
            {
               for(int j=0;j<isReady[i].length;j++)
               {
                  if(isReady[i][j] == true)
                  {
                     if (i==0&&j==0) //1번 활주로 - 1번 착륙 큐
                     {
                        Depart_Land.firstRunway.Emer_Land1_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl1EmerEnqueue();
                     }
                     else if (i==0&&j==1) //1번 활주로-2번 착륙 큐
                     {
                        Depart_Land.firstRunway.Emer_Land2_Enqueue(emerAirplane[i][j]);
                        Depart_Land.firstRunway.Setl2EmerEnqueue();
                     }
                     else if (i==1&&j==0)//2번 활주로 - 1번 착륙 큐
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
   //긴급 착륙 비행기가 있는지 없는지 3번 활주로에서 확인하는 함수
   public boolean GetisReady() 
   {
      for(int i=0;i<isReady.length;i++)       
         for(int j=0;j<isReady[i].length;j++)
         {
            if(isReady[i][j] == true) // 긴급착륙이 발생 하였다면
            {
               true_count++;          // 긴급착륙의 개수를 1 증가 시킨다.
            }
         }
      if(true_count != 0)
         return true; // 긴급 착륙 비행기가 있습니다.
      else
         return false;// 긴급 착륙 비행기가 없습니다.
   }
   public int GetEmerCount()//긴급 착륙한 비행기의 수를 반환
   {
      return emr_count;
   }
}