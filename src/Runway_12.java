package n_dep_n_land;

import java.security.AllPermission;
import java.util.Vector;
import javax.swing.*;
public class Runway_12 implements Runnable {
   //1,2번 활주로 클래스
   //1개의 이륙 큐, 2개의 착륙 큐가 필요-> 3개의 Vector을 생성
   private Vector<Airplane> d_Q;
   private Vector<Airplane> l1_Q;
   private Vector<Airplane> l2_Q;

   private int d_ind;//이륙 큐에 대한 인덱스
   private int l1_ind;//첫번째 착륙 큐에 대한 인덱스
   private int l2_ind;//두번째 착륙 큐에 대한 인덱스

   private int d_count;//이륙 큐에 들어온 비행기에 대한 카운트(긴급 착륙을 제외한)
   //l1,l2_count는 정확히 착륙을 시도했던 비행기의 수를 알기 위해서 사용
   private int l1emer_count;//1번 착륙 큐에서 긴급 착륙한 비행기 대수
   private int l2emer_count;//2번 착륙 큐에서 긴급 착륙한 비행기 대수
   private int accident_count;//사고가 일어난 카운트
   private int l1_acc_count;//1번째 착륙 큐에서 발생한 사고 횟수
   private int l2_acc_count;//2번째 착륙 큐에서 발생한 사고 횟수

   private int l1_return_emer_count;//1번째 착륙 큐에서 비상 착륙을 위해 큐 밖으로 나간 횟수
   private int l2_return_emer_count;//2번째 착륙 큐에서 비상 착륙을 위해 큐 밖으로 나간 횟수

   private String d_enq_status="";//이륙 큐에 Enqueue가 일어났음
   private String d_deq_status="";//이륙 큐에 Dequeue가 일어났음

   private String l1_enq_status="";//1번째 착륙 큐에 비행기가 들어옴
   private String l1_acc_status="";//1번째 착륙 큐에서 사고가 일어났음
   private String l1_deq_status="";//1번째 착륙 큐에 Dequeue가 일어났음
   private String l1_emer_status="";//1번째 착륙 큐에 긴급 착륙이 필요한 비행기가 발생

   private String l2_enq_status="";//2번째 착륙 큐에 비행기가 들어옴
   private String l2_acc_status="";//2번째 착륙 큐에서 사고가 일어났음
   private String l2_deq_status="";//2번째 착륙 큐에 Dequeue가 일어났음
   private String l2_emer_status="";//2번째 착륙 큐에 긴급 착륙이 필요한 비행기가 발생함

   private int d_waitTotal;//이륙한 비행기들의 대기 시간 합
   private int l1_waitTotal;//1번째 착륙 큐에 착륙한 비행기들의 대기 시간 합
   private int l1_remTimeTotal;//1번째 착륙 큐에 착륙한 비행기들의 남은 비행 시간 합
   private int l2_waitTotal;//2번째 작륙 큐에 착륙한 비행기들의 대기 시간 합
   private int l2_remTimeTotal;//2번째 착륙 큐에 착륙한 비행기들의 남은 비행 시간 합

   private boolean l1_isEmerEnqueue;//1번 착륙 큐에 긴급 착륙 비행기가 들어왔는지 확인하는 변수
   private boolean l2_isEmerEnqueue;//2번 착륙 큐에 긴급 착륙 비행기가 들어왔는지 확인하는 변수

   private JTextArea dep_ta;
   private JTextArea land_ta1;
   private JTextArea land_ta2;

   private Airplane Emergency;

   private int first_thread_Time;   // 첫번째 스레드에서 시간 변수
   private int second_thread_Time;  // 두번쨰 스레드에서 시간 변수

   SharedMemory ShareAirplane = new SharedMemory();

   public Runway_12(JTextArea dep_ta1, JTextArea land_ta1, JTextArea land_ta2)
   {
      this.dep_ta=dep_ta1;
      this.land_ta1=land_ta1;
      this.land_ta2=land_ta2;
      d_enq_status="";//이륙 큐에 Enqueue가 일어났음
      //각 큐를 새로 생성
      d_Q=new Vector<Airplane>();//TextArea 2개(1번활주로),2번활주로
      l1_Q=new Vector<Airplane>();//TextArea 2개(1번, 2번)
      l2_Q=new Vector<Airplane>();//TextArea 2개(1번,2번)

      //인덱스 초기화
      d_ind=l1_ind=l2_ind=0;
      //각종 변수들 초기화
      d_count=l1_return_emer_count=l2_return_emer_count=0;
      accident_count=l1_acc_count=l2_acc_count=l1emer_count=l2emer_count=0;
      d_waitTotal=l1_waitTotal=l1_remTimeTotal=l2_waitTotal=l2_remTimeTotal=0;
      //메시지 초기화
      //긴급 착륙 메시지는 따로 수정할 것이 없으니 초기화하여 공통적으로 사용

      first_thread_Time = 0;
      second_thread_Time = 0;

      l1_isEmerEnqueue=false;
      l2_isEmerEnqueue=false;

   }
   public void run() 
   {
      // 작업 공간 분리해놈
      /* 1번 활주로 스레드 입니다*/
      if(Thread.currentThread() == Depart_Land.first_th)
      {
         first_thread_Time = 0;
         while(first_thread_Time < Depart_Land.PlayTime)
         {
            if ((first_thread_Time!=0 && first_thread_Time%50==0)||first_thread_Time==0)
            {
               dep_ta.setText("");
               land_ta1.setText("");
               land_ta2.setText("");
            }
            String old=dep_ta.getText();
            dep_ta.setText("");
            dep_ta.append(first_thread_Time+"분: \n");
            dep_ta.append(old);

            old=land_ta1.getText();
            land_ta1.setText("");
            land_ta1.append(first_thread_Time+"분: \n");
            land_ta1.append(old);

            old=land_ta2.getText();
            land_ta2.setText("");
            land_ta2.append(first_thread_Time+"분: \n");
            land_ta2.append(old);

            Depart_Land.firstRunway.Land1_renewal(); // 첫번째 착륙 큐 연료/시간 계산
            Depart_Land.firstRunway.Land2_renewal(); // 두번째 착륙 큐 연료/시간 계산

            Depart_Land.firstRunway.Check_LandQ1();
            Depart_Land.firstRunway.Check_LandQ2();

            //************************************긴급 착륙 비행기가 들어온 경우
            if (l1_isEmerEnqueue||l2_isEmerEnqueue)//1번 착륙 큐에 긴급 착륙 비행기가 들어왔을 경우
            {
               if (l1_isEmerEnqueue)//1번 착륙 큐에 긴급 착륙 비행기가 들어왔을 경우
               {
                  Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                  l1_isEmerEnqueue=false;//1번 착륙 큐에 긴급 착륙 비행기가 착륙하였음
               }
               //무조건 1번 착륙 큐에 대해서 착륙을 수행(이륙이나 2번 착륙 큐에 대해서 수행 안 함)
               else//2번 착륙 큐에 긴급 착륙 비행기가 들어왔을 경우
               {
                  Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                  l2_isEmerEnqueue=false;//2번 착륙 큐에 긴급 착륙 비행기가 착륙하였음
               }
               //무조건 2번 착륙 큐에 대해서 착륙을 수행(이륙이나 1번 착륙 큐에 대해서 수행 안 함)
            }
            //************************************긴급 착륙 비행기가 들어오지 않았을 경우
            else
            {
               int select=(int)(Math.random()*3)+1;//1~3까지의 난수 생성
               switch(select)
               {
               case 1://이륙 큐가 선택됨
                  if (d_Q.size()!=0&&d_ind<d_Q.size())//이륙 큐에 비행기가 있다면
                  {   
                     Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//이륙 수행

                     break;
                  }
                  else//없으면
                  {
                     if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1번 착륙 큐를 검사-비행기가 있으면
                     {
                        Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                        break;
                     }
                     else if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1번 착륙 큐에도 비행기가 없고 2번 착륙 큐에 비행기가 있으면
                     {   
                        Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                        break;
                     }
                  }
                  break;
               case 2:
                  //1번 착륙 큐에 비행기가 있을 경우 1번 착륙 큐에 대한 착륙 수행
                  //없으면 비행기가 있는 큐에 대해서 이륙이나 착륙 수행
                  if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1번 착륙 큐를 검사-비행기가 있으면
                  {
                     Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                     break;
                  }
                  else
                  {
                     if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1번 착륙 큐에도 비행기가 없고 2번 착륙 큐에 비행기가 있으면
                     {   
                        Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                        break;
                     }
                     else if (d_Q.size()!=0&&d_ind<d_Q.size())
                     {   
                        Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//이륙 수행

                        break;
                     }
                  }
                  break;
               case 3:
                  //2번 착륙 큐에 비행기가 있을 경우 2번 착륙 큐에 대한 착륙 수행
                  //없으면 비행기가 있는 큐에 대해서 이륙이나 착륙 수행
                  if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
                  {
                     Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                     break;
                  }
                  else
                  {
                     if (d_Q.size()!=0&&d_ind<d_Q.size())//이륙 큐에 비행기가 있다면
                     {
                        Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//이륙 수행

                        break;
                     }
                     else if (l1_Q.size()!=0&&l1_ind<l1_Q.size())
                     {
                        Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                        break;
                     }
                  }
                  break;
               }
            }
            try
            {
               Thread.sleep(1000/Depart_Land.Speed);
            }
            catch(InterruptedException e1) {return;}
            catch(IllegalArgumentException e2) {return;}
            first_thread_Time++;
         }
         String old=dep_ta.getText();
         dep_ta.setText("");
         dep_ta.append(first_thread_Time+"분 :\n");
         dep_ta.append(old);

         old=land_ta1.getText();
         land_ta1.setText("");
         land_ta1.append(first_thread_Time+"분 :\n");
         land_ta1.append(old);

         old=land_ta2.getText();
         land_ta2.setText("");
         land_ta2.append(first_thread_Time+"분 :\n");
         land_ta2.append(old);
      }
      /* 2번 활주로 스레드 입니다.*/
      else if(Thread.currentThread() == Depart_Land.second_th)
      {
         second_thread_Time = 0;
         while(second_thread_Time < Depart_Land.PlayTime)
         {

            if ((second_thread_Time!=0 && second_thread_Time%50==0)||second_thread_Time==0)
            {
               dep_ta.setText("");
               land_ta1.setText("");
               land_ta2.setText("");
            }
            String old=dep_ta.getText();
            dep_ta.setText("");
            dep_ta.append(second_thread_Time+"분: \n");
            dep_ta.append(old);

            old=land_ta1.getText();
            land_ta1.setText("");
            land_ta1.append(second_thread_Time+"분: \n");
            land_ta1.append(old);

            old=land_ta2.getText();
            land_ta2.setText("");
            land_ta2.append(second_thread_Time+"분: \n");
            land_ta2.append(old);

            Depart_Land.secondRunway.Land1_renewal(); // 연료/시간 다시 계산
            Depart_Land.secondRunway.Land2_renewal();

            Depart_Land.secondRunway.Check_LandQ1();
            Depart_Land.secondRunway.Check_LandQ2();

            //************************************긴급 착륙 비행기가 들어올 경우
            if (l1_isEmerEnqueue||l2_isEmerEnqueue)
            {
               if (l1_isEmerEnqueue)//1번 착륙 큐에 긴급 착륙 비행기가 들어왔을 경우
               {
                  this.Land1_Dequeue(second_thread_Time);
                  //                  Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                  l1_isEmerEnqueue=false;//1번 착륙 큐에 긴급 착륙 비행기가 착륙하였음
               }
               //무조건 1번 착륙 큐에 대해서 착륙을 수행(이륙이나 2번 착륙 큐에 대해서 수행 안 함)
               else//2번 착륙 큐에 긴급 착륙 비행기가 들어왔을 경우
               {
                  this.Land2_Dequeue(second_thread_Time);
                  //                  Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                  l2_isEmerEnqueue=false;//2번 착륙 큐에 긴급 착륙 비행기가 착륙하였음
               }
               //무조건 2번 착륙 큐에 대해서 착륙을 수행(이륙이나 1번 착륙 큐에 대해서 수행 안 함)

            }
            //************************************긴급 착륙 비행기가 들어오지 않았을 경우
            else
            {
               int select=(int)(Math.random()*3)+1;//1~3까지의 난수 생성
               switch(select)
               {
               case 1://이륙 큐가 선택됨
                  if (d_Q.size()!=0&&d_ind<d_Q.size())//이륙 큐에 비행기가 있다면
                  {
                     Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//이륙 수행
                     break;
                  }
                  else//없으면
                  {
                     if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1번 착륙 큐를 검사-비행기가 있으면
                     {
                        Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                        break;
                     }
                     else if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1번 착륙 큐에도 비행기가 없고 2번 착륙 큐에 비행기가 있으면
                     {
                        Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                        break;
                     }
                  }

                  break;
               case 2:
                  //1번 착륙 큐에 비행기가 있을 경우 1번 착륙 큐에 대한 착륙 수행
                  //없으면 비행기가 있는 큐에 대해서 이륙이나 착륙 수행
                  if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1번 착륙 큐를 검사-비행기가 있으면
                  {
                     Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                     break;
                  }
                  else
                  {
                     if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1번 착륙 큐에도 비행기가 없고 2번 착륙 큐에 비행기가 있으면
                     {
                        Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                        break;
                     }
                     else if (d_Q.size()!=0&&d_ind<d_Q.size())
                     {
                        Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//이륙 수행
                        break;
                     }
                  }

                  break;
               case 3:
                  //2번 착륙 큐에 비행기가 있을 경우 2번 착륙 큐에 대한 착륙 수행
                  //없으면 비행기가 있는 큐에 대해서 이륙이나 착륙 수행
                  if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
                  {
                     Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                     break;
                  }
                  else
                  {
                     if (d_Q.size()!=0&&d_ind<d_Q.size())//이륙 큐에 비행기가 있다면
                     {
                        Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//이륙 수행
                        break;
                     }
                     else if (l1_Q.size()!=0&&l1_ind<l1_Q.size())
                     {
                        Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                        break;                     
                     }
                  }

                  break;
               }
            }
            try
            {
               Thread.sleep(1000/Depart_Land.Speed);
            }
            catch(InterruptedException e1) {return;}
            catch(IllegalArgumentException e2) {return;}
            second_thread_Time++;
         }
         String old=dep_ta.getText();
         dep_ta.setText("");
         dep_ta.append(second_thread_Time+"분 :\n");
         dep_ta.append(old);

         old=land_ta1.getText();
         land_ta1.setText("");
         land_ta1.append(second_thread_Time+"분 :\n");
         land_ta1.append(old);

         old=land_ta2.getText();
         land_ta2.setText("");
         land_ta2.append(second_thread_Time+"분 :\n");
         land_ta2.append(old);
      }
   }
   //이륙 큐에 새로운 비행기가 들어오는 함수
   public void Dep_Enqueue(int present_time, int count, int selector)
   {//매개 변수로 현재 시간을 전달받음
      for (int i=0; i<count; i++)
         d_Q.add(new Airplane(present_time, 1));
      d_count+=count;
      d_enq_status=count+"대의 비행기가 이륙 요청\n";
      if(selector==1)
      {
         String old=   Depart_Land.firstRunway.dep_ta.getText();
         Depart_Land.firstRunway.dep_ta.setText("");
         Depart_Land.firstRunway.dep_ta.append(d_enq_status);
         Depart_Land.firstRunway.dep_ta.append(old);
      }
      else if(selector==2)
      {
         String old=   Depart_Land.secondRunway.dep_ta.getText();
         Depart_Land.secondRunway.dep_ta.setText("");
         Depart_Land.secondRunway.dep_ta.append(d_enq_status);
         Depart_Land.secondRunway.dep_ta.append(old);
      }
   }
   //착륙 큐에 긴급 착륙하는 비행기를 전달받는 함수
   //어차피 긴급 착륙은 최소 10분 이후에 발생하므로(연료량 최소 20, 비행 가능 시간 : 연료량/2)
   //큐가 비어 있는지 검사할 필요 없음
   //1번째 착륙 큐에 새로운 비행기가 들어오는 함수
   public void Land1_Enqueue(int present_time, int count, int selector)
   {//매개 변수로 현재 시간을 전달받음
      for (int i=0; i<count; i++)
         l1_Q.add(new Airplane(present_time,2));
      l1_enq_status=count+"대의 비행기가 착륙 요쳥\n";
      if(selector == 1)
      {
         String old=   Depart_Land.firstRunway.land_ta1.getText();
         Depart_Land.firstRunway.land_ta1.setText("");
         Depart_Land.firstRunway.land_ta1.append(l1_enq_status);
         Depart_Land.firstRunway.land_ta1.append(old);
      }
      else if (selector == 3)
      {
         String old=   Depart_Land.secondRunway.land_ta1.getText();
         Depart_Land.secondRunway.land_ta1.setText("");
         Depart_Land.secondRunway.land_ta1.append(l1_enq_status);
         Depart_Land.secondRunway.land_ta1.append(old);
      }
   }
   public void Emer_Land1_Enqueue(Airplane Emer)//긴급 착륙 비행기가 1번 착륙 큐로 들어옴
   {
      l1_Q.add(l1_ind,Emer);//현재 인덱스로 끼어들기함
   }
   public void Emer_Land2_Enqueue(Airplane Emer)//긴급 착륙 비행기가 1번 착륙 큐로 들어옴
   {
      l2_Q.add(l2_ind,Emer);//현재 인덱스로 끼어들기함
   }
   //2번째 착륙 큐에 새로운 비행기가 들어오는 함수
   public void Land2_Enqueue(int present_time, int count, int selector)
   {//매개 변수로 현재 시간을 전달받음
      for (int i=0; i<count; i++)
         l2_Q.add(new Airplane(present_time,2));
      l2_enq_status=count+"대의 비행기가 착륙 요쳥\n";
      if(selector == 2)
      {
         String old=   Depart_Land.firstRunway.land_ta2.getText();
         Depart_Land.firstRunway.land_ta2.setText("");
         Depart_Land.firstRunway.land_ta2.append(l2_enq_status);
         Depart_Land.firstRunway.land_ta2.append(old);
      }
      else if (selector == 4)
      {
         String old=   Depart_Land.secondRunway.land_ta2.getText();
         Depart_Land.secondRunway.land_ta2.setText("");
         Depart_Land.secondRunway.land_ta2.append(l2_enq_status);
         Depart_Land.secondRunway.land_ta2.append(old);
      }
   }
   //***********************************************
   //이륙 큐에 대한 Dequeue
   public void Dep_Dequeue(int present_time)//이륙 큐에 대한 Dequeue
   {//이륙 시간과 대기 시간을 계산하기 위해 현재 시간을 매개 변수로 받음
      if (d_Q.size()!=0&&d_ind<d_Q.size())
      {
         d_Q.get(d_ind).SetDeQ(present_time);
         d_waitTotal+=d_Q.get(d_ind).GetWTime();//평균 대기 시간 더함

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            d_deq_status=d_Q.get(d_ind).GetNumber()+" 비행기가 이륙함\n";
            String old=   this.dep_ta.getText();
            this.dep_ta.setText("");
            this.dep_ta.append(d_deq_status);
            this.dep_ta.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            d_deq_status=d_Q.get(d_ind).GetNumber()+" 비행기가 이륙함\n";
            String old=   this.dep_ta.getText();
            this.dep_ta.setText("");
            this.dep_ta.append(d_deq_status);
            this.dep_ta.append(old);
         }
         d_ind++;//다음 인덱스로 이동
      }
   }
   public void Land1_Dequeue(int present_time)//첫번째 착륙 큐에 대한 Dequeue
   {
      if (l1_Q.size()!=0&&l1_ind<l1_Q.size())
      {
         l1_Q.get(l1_ind).SetDeQ(present_time);//착륙 시간과 대기 시간 저장
         l1_waitTotal+=l1_Q.get(l1_ind).GetWTime();//평균 대기 시간에 대기 시간 추가
         l1_remTimeTotal+=l1_Q.get(l1_ind).GetRemainTime();//평균 비행 가능 시간에 추가
         if (l1_Q.get(l1_ind).GetEmergencyStatus()==true)//긴급 착륙 비행기이면
         {
            l1_deq_status="★★★"+l1_Q.get(l1_ind).GetNumber()+" 비행기가 긴급 착륙함\n";
            l1emer_count++;//긴급 착륙 카운트 증가시킴
         }
         else//일반 착륙 비행기이면
            l1_deq_status=l1_Q.get(l1_ind).GetNumber()+" 비행기가 착륙함\n";

         l1_ind++;
         if(Thread.currentThread() == Depart_Land.first_th)
         {
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_deq_status);
            this.land_ta1.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            String old=this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_deq_status);
            this.land_ta1.append(old);
         }
      }
   }
   public void Land2_Dequeue(int present_time)//첫번째 착륙 큐에 대한 Dequeue
   {
      if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
      {
         l2_Q.get(l2_ind).SetDeQ(present_time);//착륙 시간과 대기 시간 저장
         l2_waitTotal+=l2_Q.get(l2_ind).GetWTime();//평균 대기 시간에 대기 시간 추가
         l2_remTimeTotal+=l2_Q.get(l2_ind).GetRemainTime();//평균 비행 가능 시간에 추가

         if (l2_Q.get(l2_ind).GetEmergencyStatus()==true)//긴급 착륙 비행기이면
         {
            l2_deq_status="★★★"+l2_Q.get(l2_ind).GetNumber()+" 비행기가 긴급 착륙함\n";
            l2emer_count++;//긴급 착륙 카운트 증가시킴
         }
         else//일반 착륙 비행기이면
            l2_deq_status=l2_Q.get(l2_ind).GetNumber()+" 비행기가 착륙함\n";

         l2_ind++;//다음 인덱스로 이동
         if(Thread.currentThread() == Depart_Land.first_th)
         {
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_deq_status);
            this.land_ta2.append(old);

         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_deq_status);
            this.land_ta2.append(old);
         }
      }
   }
   //**********************************************
   //긴급 착륙을 시도해야 하는 비행기가 있는지, 그리고 사고 비행기가 있는지 검사하는 함수
   public void Check_LandQ1()
   {
      int emer_ind1=-1;//비상 착륙이 필요한 비행기의 인덱스(1번째 착륙 큐)
      int acc_count=0;//해당 단위 시간에 몇 개의 사고 비행기가 발생하였는가를 확인함
      Emergency=new Airplane(-1,2);
      if (l1_Q.size()!=0)//큐가 비어있지 않으면
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            if (l1_Q.get(i).GetRemainTime()==1)
            {//만약 긴급 착륙이 필요한 비행기가 있다면
               l1_return_emer_count++;
               emer_ind1=i;//해당 인덱스로 맞춰주고
               break;//탈출
            }
         }
      }
      if (l1_Q.size()!=0)//큐가 비어있지 않으면
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            if (l1_Q.get(i).GetFuel()<2)//연료가 다 떨어진 비행기가 큐에 남아있으면?
            {//사고지 뭐...
               l1_Q.remove(i);
               accident_count++;
               l1_acc_count++;
               acc_count++;
            }   //연료량이 -인 비행기는 바로 그 자리에서 카운트 짤라내고 사고 처리해버리기.
         }
      }
      if (acc_count!=0)
      {

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            l1_acc_status="사고 비행기가 "+acc_count+"대 발생함\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_acc_status);
            this.land_ta1.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            l1_acc_status="사고 비행기가 "+acc_count+"대 발생함\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_acc_status);
            this.land_ta1.append(old);
         }
      }
      if (emer_ind1!=-1&&emer_ind1<l1_Q.size())
      {
         Emergency=l1_Q.get(emer_ind1);
         Emergency.SetEmerPriority();//우선 순위 변경하고
         Emergency.SetEmergency();//긴급하다고 바꿈

         if(Thread.currentThread() == Depart_Land.first_th) // 사고발생이 1번 활주로 스레드에서 일어났다면
         {
            l1_emer_status="긴급 착륙 비행기가 발생함\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_emer_status);
            this.land_ta1.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,1.1); // 긴급 착륙 비행기를 세팅
         }
         if(Thread.currentThread() == Depart_Land.second_th)// 사고발생이 2번 활주로 스레드에서 일어났다면
         {
            l1_emer_status="긴급 착륙 비행기가 발생함\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_emer_status);
            this.land_ta1.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,2.1); // 긴급 착륙 비행기를 세팅
         }

         l1_Q.remove(emer_ind1);//복사 후 해당 비행기는 제거
      }      
   }
   public synchronized void Check_LandQ2()
   {
      int emer_ind2=-1;
      int acc_count=0;//해당 단위 시간에 몇 개의 사고 비행기가 발생하였는가를 확인함
      Airplane Emergency=new Airplane(-1,2);
      if (l2_Q.size()!=0)
      {
         for (int i=l2_ind; i<l2_Q.size(); i++)
         {
            if (l2_Q.get(i).GetRemainTime()==2)
            {//만약 긴급 착륙이 필요한 비행기가 있다면
               emer_ind2=i;//해당 인덱스로 맞춰주고
               l2_return_emer_count++;//큐 밖으로 나가는 긴급 착륙 비행기 횟수 증가
               break;//탈출
            }
         }
      }
      if (l2_Q.size()!=0)//큐가 비어있지 않으면
      {
         for (int i=l2_ind; i<l2_Q.size(); i++)
         {
            if (l2_Q.get(i).GetFuel()<2)//연료가 다 떨어진 비행기가 큐에 남아있으면?
            {//사고지 뭐...
               l2_Q.remove(i);
               accident_count++;
               l2_acc_count++;
               acc_count++;
            }   //연료량이 -인 비행기는 바로 그 자리에서 짤라내고 사고 처리해버리기.
         }
      }
      if (acc_count!=0)
      {

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            l2_acc_status="사고 비행기가 "+acc_count+"대 발생함\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_acc_status);
            this.land_ta2.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            l2_acc_status="사고 비행기가 "+acc_count+"대 발생함\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_acc_status);
            this.land_ta2.append(old);
         }
      }
      //이후 전달받은 비행기의 Enqueue 시간을 검사하여 비상 착륙을 시도하는 비행기가
      //있는지 없는지를 검사(-1이면 비상 착륙을 시도하는 비행기가 없었음을 의미)
      if (emer_ind2!=-1&&emer_ind2<l2_Q.size())
      {
         Emergency=l2_Q.get(emer_ind2);
         Emergency.SetEmerPriority();//우선 순위 변경하고
         Emergency.SetEmergency();//긴급 착륙으로 변경하고

         if(Thread.currentThread() == Depart_Land.first_th) // 사고발생이 1번 활주로스레드에서 일어났다면
         {
            l2_emer_status="긴급 착륙 비행기가 발생함\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_emer_status);
            this.land_ta2.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,1.2); // 긴급 착륙 비행기를 세팅해주고
         }
         if(Thread.currentThread() == Depart_Land.second_th)// 사고발생이 2번 활주로스레드에서 일어났다면
         {
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_emer_status);
            this.land_ta2.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,2.2); // 긴급 착륙 비행기를 세팅해주고
         }

         l2_Q.remove(emer_ind2);//긴급 착륙해야되니까 착륙 큐에서 제거함
      }


   }
   //***********************************************
   //각 큐의 사이즈를 비슷하게 유지해야 한다고 했으므로
   //큐의 길이를 시뮬레이터로 전달하는 함수가 필요
   public int SizeofDefQ()//이륙 큐의 사이즈를 반환하는 함수
   {
      if (d_Q.size()!=0)
         return d_Q.size();//큐에 한 대의 비행기 이상이 들어온 적이 있다면
      else//큐에 한 대의 비행기조차 들어온 적이 없다면
         return 0;
   }
   public int SizeofLandQ_1()//첫번째 착륙 큐
   {
      if (l1_Q.size()!=0)//큐에 한 대의 비행기 이상이 들어온 적이 있다면
         return l1_Q.size();
      else//큐에 한 대의 비행기조차 들어온 적이 없다면
         return 0;
   }
   public int SizeofLandQ_2()//두번째 착륙 큐의 사이즈를 반환하는 함수
   {
      if(l2_Q.size()!=0)//큐에 한 대의 비행기 이상이 들어온 적이 있다면
         return l2_Q.size();
      else//큐에 한 대의 비행기조차 들어온 적이 없다면
         return 0;
   }
   public int LandQSizeaver()//(a+b)/2를 미리 계산해놨음
   {
      return (l1_Q.size()+l2_Q.size())/2;
   }
   //***********************************************
   //착륙 큐의 연료량과 비행 가능 시간 갱신
   public void Land1_renewal()
   {
      if (l1_Q.size()!=0)//큐가 비어있지 않다면
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            l1_Q.get(i).ReduceFuel();//연료량 2감소
            l1_Q.get(i).OperationRemainTime();//감소시킨 연료량 갖고 남은 시간 계산
         }
      }
   }
   public void Land2_renewal()
   {
      if (l2_Q.size()!=0)
      {
         for (int i=l2_ind; i<l2_Q.size(); i++)
         {
            l2_Q.get(i).ReduceFuel();
            l2_Q.get(i).OperationRemainTime();
         }
      }
   }
   //***********************************************
   //평균 시간 반환
   //세 활주로에 대해서 평균을 계산해야 하므로 하나의 활주로 내에서 계산하여 반환하면 안 됨
   //(x/x')+(y/y')+(z/z')가 아니라 (x+y+z)/(x'+y'+z')로 계산해야 함
   public int GetDepWtime()//이륙한 비행기들의 이륙 대기 시간 반환
   {
      return d_waitTotal;
   }
   public int GetLandWtime()//활주로에 착륙한 비행기들의 착륙 대기 시간 반환
   {
      return l1_waitTotal+l2_waitTotal;
   }
   public int GetLandRtime()//활주로에 착륙한 비행기들의 남은 비행 가능 시간 반환
   {
      return l1_remTimeTotal+l2_remTimeTotal;
   }
   public int GetLandIndex()//착륙 큐의 현재 인덱스를 반환
   //평균 시간을 계산하기 위해서도 필요하지만
   {//최종 결과 중 긴급 착륙 비행기 비율을 계산할 때에도 필요
      return l1_ind+l2_ind;
   }
   //***********************************************
   //긴급 착륙에서 4개의 비행기가 들어왔을 때 하나 짤라내야 하므로 사고 카운트를 외부에서 증가시킬 무언가가 필요
   //이때 사고로 처리되는 비행기는 2번째 활주로의 2번 착륙 큐이므로 2번째 착륙 큐의 사고 카운트를 증가시킬 함수만 있으면 됨
   public void IncrL2AccCount()//2번째 착륙 큐의 사고 카운트 1 증가
   {
      l2_acc_count++;
   }
   //***********************************************
   public void Setl1EmerEnqueue()//1번 착륙 큐에 긴급 착륙 비행기가 들어옴
   {
      l1_isEmerEnqueue=true;
   }
   public void Setl2EmerEnqueue()//1번 착륙 큐에 긴급 착륙 비행기가 들어옴
   {
      l2_isEmerEnqueue=true;
   }
   //***********************************************
   //그 외에 카운트 변수들 반환
   public int GetDepEnqeueCount()//이륙 큐에 들어온(이륙 유무 상관 없이) 비행기 수 반환
   {
      return d_count;
   }
   public int GetL1EmerDequeueCount()//1번째 착륙 큐에서 긴급 착륙한 비행기 수를 반환
   {
      return l1emer_count;
   }
   public int GetL2EmerDequeueCount()//2번쨰 착륙 큐에서 긴급 착륙한 비행기 수를 반환
   {
      return l2emer_count;
   }
   public int GetEmerDequeueCount()//해당 활주로에서 긴급 착륙한 모든 비행기 수를 반환
   {
      return l1emer_count+l2emer_count;
   }
   public int GetAccidentCount()//사고가 일어난 비행기 수를 반환
   {
      return accident_count;
   }
   public int L1NotDequeuedCount()//1번째 착륙 큐에 남아 있는 비행기 수 반환
   {
      return l1_Q.size()-l1_ind;
   }
   public int L2NotDequeuedCount()//2번째 착륙 큐에 남아 있는 비행기 수 반환
   {
      return l2_Q.size()-l2_ind;
   }
   public int GetLand1EnqueueCount()//1번 착륙 큐에 들어온 모든 비행기 수 반환
   {
      return l1_return_emer_count+l1_acc_count+(l1_Q.size()-l1_ind)-l1emer_count+l1_ind;
   }
   public int GetLand2EnqueueCount()//2번 착륙 큐에 들어온 모든 비행기 수 반환
   {
      return l2_return_emer_count+l2_acc_count+(l2_Q.size()-l2_ind)-l2emer_count+l2_ind;
   }
   public int GetL1ReturnEmerCount()//1번 착륙 큐에서 긴급 착륙을 위해 나간 비행기의 카운트 반환
   {
      return l1_return_emer_count;
   }
   public int GetL2ReturnEmerCount()//2번 착륙 큐에서 긴급 착륙을 위해 나간 비행기의 카운트 반환
   {
      return l2_return_emer_count;
   }
   public int GetL1AccCount()//1번 착륙 큐의 사고 카운트 반환
   {
      return l1_acc_count;
   }
   public int GetL2AccCount()//2번 착륙 큐의 사고 카운트 반환
   {
      return l2_acc_count;
   }
   //인덱스 반환
   //통계 출력에 필요
   public int GetLand1Index()//1번째 착륙 큐의 현재 인덱스 반환
   {
      return l1_ind;
   }
   public int GetLand2Index()//2번째 착륙 큐의 현재 인덱스 반환
   {
      return l2_ind;
   }
   public int GetDepIndex()//이륙 큐의 현재 인덱스 반환(이륙한 비행기 수)
   {
      return d_ind;
   }
   public int Get1stThreadTime()//1번 활주로 스레드의 진행 시간 반환
   {
      return first_thread_Time;
   }
   public int Get2ndThreadTime()//2번 활주로 스레드의 진행 시간 반환
   {
      return second_thread_Time;
   }
}