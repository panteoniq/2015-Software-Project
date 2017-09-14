package n_dep_n_land;

import java.security.AllPermission;
import java.util.Vector;
import javax.swing.*;
public class Runway_12 implements Runnable {
   //1,2�� Ȱ�ַ� Ŭ����
   //1���� �̷� ť, 2���� ���� ť�� �ʿ�-> 3���� Vector�� ����
   private Vector<Airplane> d_Q;
   private Vector<Airplane> l1_Q;
   private Vector<Airplane> l2_Q;

   private int d_ind;//�̷� ť�� ���� �ε���
   private int l1_ind;//ù��° ���� ť�� ���� �ε���
   private int l2_ind;//�ι�° ���� ť�� ���� �ε���

   private int d_count;//�̷� ť�� ���� ����⿡ ���� ī��Ʈ(��� ������ ������)
   //l1,l2_count�� ��Ȯ�� ������ �õ��ߴ� ������� ���� �˱� ���ؼ� ���
   private int l1emer_count;//1�� ���� ť���� ��� ������ ����� ���
   private int l2emer_count;//2�� ���� ť���� ��� ������ ����� ���
   private int accident_count;//��� �Ͼ ī��Ʈ
   private int l1_acc_count;//1��° ���� ť���� �߻��� ��� Ƚ��
   private int l2_acc_count;//2��° ���� ť���� �߻��� ��� Ƚ��

   private int l1_return_emer_count;//1��° ���� ť���� ��� ������ ���� ť ������ ���� Ƚ��
   private int l2_return_emer_count;//2��° ���� ť���� ��� ������ ���� ť ������ ���� Ƚ��

   private String d_enq_status="";//�̷� ť�� Enqueue�� �Ͼ��
   private String d_deq_status="";//�̷� ť�� Dequeue�� �Ͼ��

   private String l1_enq_status="";//1��° ���� ť�� ����Ⱑ ����
   private String l1_acc_status="";//1��° ���� ť���� ��� �Ͼ��
   private String l1_deq_status="";//1��° ���� ť�� Dequeue�� �Ͼ��
   private String l1_emer_status="";//1��° ���� ť�� ��� ������ �ʿ��� ����Ⱑ �߻�

   private String l2_enq_status="";//2��° ���� ť�� ����Ⱑ ����
   private String l2_acc_status="";//2��° ���� ť���� ��� �Ͼ��
   private String l2_deq_status="";//2��° ���� ť�� Dequeue�� �Ͼ��
   private String l2_emer_status="";//2��° ���� ť�� ��� ������ �ʿ��� ����Ⱑ �߻���

   private int d_waitTotal;//�̷��� �������� ��� �ð� ��
   private int l1_waitTotal;//1��° ���� ť�� ������ �������� ��� �ð� ��
   private int l1_remTimeTotal;//1��° ���� ť�� ������ �������� ���� ���� �ð� ��
   private int l2_waitTotal;//2��° �۷� ť�� ������ �������� ��� �ð� ��
   private int l2_remTimeTotal;//2��° ���� ť�� ������ �������� ���� ���� �ð� ��

   private boolean l1_isEmerEnqueue;//1�� ���� ť�� ��� ���� ����Ⱑ ���Դ��� Ȯ���ϴ� ����
   private boolean l2_isEmerEnqueue;//2�� ���� ť�� ��� ���� ����Ⱑ ���Դ��� Ȯ���ϴ� ����

   private JTextArea dep_ta;
   private JTextArea land_ta1;
   private JTextArea land_ta2;

   private Airplane Emergency;

   private int first_thread_Time;   // ù��° �����忡�� �ð� ����
   private int second_thread_Time;  // �ι��� �����忡�� �ð� ����

   SharedMemory ShareAirplane = new SharedMemory();

   public Runway_12(JTextArea dep_ta1, JTextArea land_ta1, JTextArea land_ta2)
   {
      this.dep_ta=dep_ta1;
      this.land_ta1=land_ta1;
      this.land_ta2=land_ta2;
      d_enq_status="";//�̷� ť�� Enqueue�� �Ͼ��
      //�� ť�� ���� ����
      d_Q=new Vector<Airplane>();//TextArea 2��(1��Ȱ�ַ�),2��Ȱ�ַ�
      l1_Q=new Vector<Airplane>();//TextArea 2��(1��, 2��)
      l2_Q=new Vector<Airplane>();//TextArea 2��(1��,2��)

      //�ε��� �ʱ�ȭ
      d_ind=l1_ind=l2_ind=0;
      //���� ������ �ʱ�ȭ
      d_count=l1_return_emer_count=l2_return_emer_count=0;
      accident_count=l1_acc_count=l2_acc_count=l1emer_count=l2emer_count=0;
      d_waitTotal=l1_waitTotal=l1_remTimeTotal=l2_waitTotal=l2_remTimeTotal=0;
      //�޽��� �ʱ�ȭ
      //��� ���� �޽����� ���� ������ ���� ������ �ʱ�ȭ�Ͽ� ���������� ���

      first_thread_Time = 0;
      second_thread_Time = 0;

      l1_isEmerEnqueue=false;
      l2_isEmerEnqueue=false;

   }
   public void run() 
   {
      // �۾� ���� �и��س�
      /* 1�� Ȱ�ַ� ������ �Դϴ�*/
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
            dep_ta.append(first_thread_Time+"��: \n");
            dep_ta.append(old);

            old=land_ta1.getText();
            land_ta1.setText("");
            land_ta1.append(first_thread_Time+"��: \n");
            land_ta1.append(old);

            old=land_ta2.getText();
            land_ta2.setText("");
            land_ta2.append(first_thread_Time+"��: \n");
            land_ta2.append(old);

            Depart_Land.firstRunway.Land1_renewal(); // ù��° ���� ť ����/�ð� ���
            Depart_Land.firstRunway.Land2_renewal(); // �ι�° ���� ť ����/�ð� ���

            Depart_Land.firstRunway.Check_LandQ1();
            Depart_Land.firstRunway.Check_LandQ2();

            //************************************��� ���� ����Ⱑ ���� ���
            if (l1_isEmerEnqueue||l2_isEmerEnqueue)//1�� ���� ť�� ��� ���� ����Ⱑ ������ ���
            {
               if (l1_isEmerEnqueue)//1�� ���� ť�� ��� ���� ����Ⱑ ������ ���
               {
                  Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                  l1_isEmerEnqueue=false;//1�� ���� ť�� ��� ���� ����Ⱑ �����Ͽ���
               }
               //������ 1�� ���� ť�� ���ؼ� ������ ����(�̷��̳� 2�� ���� ť�� ���ؼ� ���� �� ��)
               else//2�� ���� ť�� ��� ���� ����Ⱑ ������ ���
               {
                  Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                  l2_isEmerEnqueue=false;//2�� ���� ť�� ��� ���� ����Ⱑ �����Ͽ���
               }
               //������ 2�� ���� ť�� ���ؼ� ������ ����(�̷��̳� 1�� ���� ť�� ���ؼ� ���� �� ��)
            }
            //************************************��� ���� ����Ⱑ ������ �ʾ��� ���
            else
            {
               int select=(int)(Math.random()*3)+1;//1~3������ ���� ����
               switch(select)
               {
               case 1://�̷� ť�� ���õ�
                  if (d_Q.size()!=0&&d_ind<d_Q.size())//�̷� ť�� ����Ⱑ �ִٸ�
                  {   
                     Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//�̷� ����

                     break;
                  }
                  else//������
                  {
                     if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1�� ���� ť�� �˻�-����Ⱑ ������
                     {
                        Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                        break;
                     }
                     else if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1�� ���� ť���� ����Ⱑ ���� 2�� ���� ť�� ����Ⱑ ������
                     {   
                        Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                        break;
                     }
                  }
                  break;
               case 2:
                  //1�� ���� ť�� ����Ⱑ ���� ��� 1�� ���� ť�� ���� ���� ����
                  //������ ����Ⱑ �ִ� ť�� ���ؼ� �̷��̳� ���� ����
                  if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1�� ���� ť�� �˻�-����Ⱑ ������
                  {
                     Depart_Land.firstRunway.Land1_Dequeue(first_thread_Time);
                     break;
                  }
                  else
                  {
                     if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1�� ���� ť���� ����Ⱑ ���� 2�� ���� ť�� ����Ⱑ ������
                     {   
                        Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                        break;
                     }
                     else if (d_Q.size()!=0&&d_ind<d_Q.size())
                     {   
                        Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//�̷� ����

                        break;
                     }
                  }
                  break;
               case 3:
                  //2�� ���� ť�� ����Ⱑ ���� ��� 2�� ���� ť�� ���� ���� ����
                  //������ ����Ⱑ �ִ� ť�� ���ؼ� �̷��̳� ���� ����
                  if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
                  {
                     Depart_Land.firstRunway.Land2_Dequeue(first_thread_Time);
                     break;
                  }
                  else
                  {
                     if (d_Q.size()!=0&&d_ind<d_Q.size())//�̷� ť�� ����Ⱑ �ִٸ�
                     {
                        Depart_Land.firstRunway.Dep_Dequeue(first_thread_Time);//�̷� ����

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
         dep_ta.append(first_thread_Time+"�� :\n");
         dep_ta.append(old);

         old=land_ta1.getText();
         land_ta1.setText("");
         land_ta1.append(first_thread_Time+"�� :\n");
         land_ta1.append(old);

         old=land_ta2.getText();
         land_ta2.setText("");
         land_ta2.append(first_thread_Time+"�� :\n");
         land_ta2.append(old);
      }
      /* 2�� Ȱ�ַ� ������ �Դϴ�.*/
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
            dep_ta.append(second_thread_Time+"��: \n");
            dep_ta.append(old);

            old=land_ta1.getText();
            land_ta1.setText("");
            land_ta1.append(second_thread_Time+"��: \n");
            land_ta1.append(old);

            old=land_ta2.getText();
            land_ta2.setText("");
            land_ta2.append(second_thread_Time+"��: \n");
            land_ta2.append(old);

            Depart_Land.secondRunway.Land1_renewal(); // ����/�ð� �ٽ� ���
            Depart_Land.secondRunway.Land2_renewal();

            Depart_Land.secondRunway.Check_LandQ1();
            Depart_Land.secondRunway.Check_LandQ2();

            //************************************��� ���� ����Ⱑ ���� ���
            if (l1_isEmerEnqueue||l2_isEmerEnqueue)
            {
               if (l1_isEmerEnqueue)//1�� ���� ť�� ��� ���� ����Ⱑ ������ ���
               {
                  this.Land1_Dequeue(second_thread_Time);
                  //                  Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                  l1_isEmerEnqueue=false;//1�� ���� ť�� ��� ���� ����Ⱑ �����Ͽ���
               }
               //������ 1�� ���� ť�� ���ؼ� ������ ����(�̷��̳� 2�� ���� ť�� ���ؼ� ���� �� ��)
               else//2�� ���� ť�� ��� ���� ����Ⱑ ������ ���
               {
                  this.Land2_Dequeue(second_thread_Time);
                  //                  Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                  l2_isEmerEnqueue=false;//2�� ���� ť�� ��� ���� ����Ⱑ �����Ͽ���
               }
               //������ 2�� ���� ť�� ���ؼ� ������ ����(�̷��̳� 1�� ���� ť�� ���ؼ� ���� �� ��)

            }
            //************************************��� ���� ����Ⱑ ������ �ʾ��� ���
            else
            {
               int select=(int)(Math.random()*3)+1;//1~3������ ���� ����
               switch(select)
               {
               case 1://�̷� ť�� ���õ�
                  if (d_Q.size()!=0&&d_ind<d_Q.size())//�̷� ť�� ����Ⱑ �ִٸ�
                  {
                     Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//�̷� ����
                     break;
                  }
                  else//������
                  {
                     if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1�� ���� ť�� �˻�-����Ⱑ ������
                     {
                        Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                        break;
                     }
                     else if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1�� ���� ť���� ����Ⱑ ���� 2�� ���� ť�� ����Ⱑ ������
                     {
                        Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                        break;
                     }
                  }

                  break;
               case 2:
                  //1�� ���� ť�� ����Ⱑ ���� ��� 1�� ���� ť�� ���� ���� ����
                  //������ ����Ⱑ �ִ� ť�� ���ؼ� �̷��̳� ���� ����
                  if (l1_Q.size()!=0&&l1_ind<l1_Q.size())//1�� ���� ť�� �˻�-����Ⱑ ������
                  {
                     Depart_Land.secondRunway.Land1_Dequeue(second_thread_Time);
                     break;
                  }
                  else
                  {
                     if (l2_Q.size()!=0&&l2_ind<l2_Q.size())//1�� ���� ť���� ����Ⱑ ���� 2�� ���� ť�� ����Ⱑ ������
                     {
                        Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                        break;
                     }
                     else if (d_Q.size()!=0&&d_ind<d_Q.size())
                     {
                        Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//�̷� ����
                        break;
                     }
                  }

                  break;
               case 3:
                  //2�� ���� ť�� ����Ⱑ ���� ��� 2�� ���� ť�� ���� ���� ����
                  //������ ����Ⱑ �ִ� ť�� ���ؼ� �̷��̳� ���� ����
                  if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
                  {
                     Depart_Land.secondRunway.Land2_Dequeue(second_thread_Time);
                     break;
                  }
                  else
                  {
                     if (d_Q.size()!=0&&d_ind<d_Q.size())//�̷� ť�� ����Ⱑ �ִٸ�
                     {
                        Depart_Land.secondRunway.Dep_Dequeue(second_thread_Time);//�̷� ����
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
         dep_ta.append(second_thread_Time+"�� :\n");
         dep_ta.append(old);

         old=land_ta1.getText();
         land_ta1.setText("");
         land_ta1.append(second_thread_Time+"�� :\n");
         land_ta1.append(old);

         old=land_ta2.getText();
         land_ta2.setText("");
         land_ta2.append(second_thread_Time+"�� :\n");
         land_ta2.append(old);
      }
   }
   //�̷� ť�� ���ο� ����Ⱑ ������ �Լ�
   public void Dep_Enqueue(int present_time, int count, int selector)
   {//�Ű� ������ ���� �ð��� ���޹���
      for (int i=0; i<count; i++)
         d_Q.add(new Airplane(present_time, 1));
      d_count+=count;
      d_enq_status=count+"���� ����Ⱑ �̷� ��û\n";
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
   //���� ť�� ��� �����ϴ� ����⸦ ���޹޴� �Լ�
   //������ ��� ������ �ּ� 10�� ���Ŀ� �߻��ϹǷ�(���ᷮ �ּ� 20, ���� ���� �ð� : ���ᷮ/2)
   //ť�� ��� �ִ��� �˻��� �ʿ� ����
   //1��° ���� ť�� ���ο� ����Ⱑ ������ �Լ�
   public void Land1_Enqueue(int present_time, int count, int selector)
   {//�Ű� ������ ���� �ð��� ���޹���
      for (int i=0; i<count; i++)
         l1_Q.add(new Airplane(present_time,2));
      l1_enq_status=count+"���� ����Ⱑ ���� �䫊\n";
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
   public void Emer_Land1_Enqueue(Airplane Emer)//��� ���� ����Ⱑ 1�� ���� ť�� ����
   {
      l1_Q.add(l1_ind,Emer);//���� �ε����� ��������
   }
   public void Emer_Land2_Enqueue(Airplane Emer)//��� ���� ����Ⱑ 1�� ���� ť�� ����
   {
      l2_Q.add(l2_ind,Emer);//���� �ε����� ��������
   }
   //2��° ���� ť�� ���ο� ����Ⱑ ������ �Լ�
   public void Land2_Enqueue(int present_time, int count, int selector)
   {//�Ű� ������ ���� �ð��� ���޹���
      for (int i=0; i<count; i++)
         l2_Q.add(new Airplane(present_time,2));
      l2_enq_status=count+"���� ����Ⱑ ���� �䫊\n";
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
   //�̷� ť�� ���� Dequeue
   public void Dep_Dequeue(int present_time)//�̷� ť�� ���� Dequeue
   {//�̷� �ð��� ��� �ð��� ����ϱ� ���� ���� �ð��� �Ű� ������ ����
      if (d_Q.size()!=0&&d_ind<d_Q.size())
      {
         d_Q.get(d_ind).SetDeQ(present_time);
         d_waitTotal+=d_Q.get(d_ind).GetWTime();//��� ��� �ð� ����

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            d_deq_status=d_Q.get(d_ind).GetNumber()+" ����Ⱑ �̷���\n";
            String old=   this.dep_ta.getText();
            this.dep_ta.setText("");
            this.dep_ta.append(d_deq_status);
            this.dep_ta.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            d_deq_status=d_Q.get(d_ind).GetNumber()+" ����Ⱑ �̷���\n";
            String old=   this.dep_ta.getText();
            this.dep_ta.setText("");
            this.dep_ta.append(d_deq_status);
            this.dep_ta.append(old);
         }
         d_ind++;//���� �ε����� �̵�
      }
   }
   public void Land1_Dequeue(int present_time)//ù��° ���� ť�� ���� Dequeue
   {
      if (l1_Q.size()!=0&&l1_ind<l1_Q.size())
      {
         l1_Q.get(l1_ind).SetDeQ(present_time);//���� �ð��� ��� �ð� ����
         l1_waitTotal+=l1_Q.get(l1_ind).GetWTime();//��� ��� �ð��� ��� �ð� �߰�
         l1_remTimeTotal+=l1_Q.get(l1_ind).GetRemainTime();//��� ���� ���� �ð��� �߰�
         if (l1_Q.get(l1_ind).GetEmergencyStatus()==true)//��� ���� ������̸�
         {
            l1_deq_status="�ڡڡ�"+l1_Q.get(l1_ind).GetNumber()+" ����Ⱑ ��� ������\n";
            l1emer_count++;//��� ���� ī��Ʈ ������Ŵ
         }
         else//�Ϲ� ���� ������̸�
            l1_deq_status=l1_Q.get(l1_ind).GetNumber()+" ����Ⱑ ������\n";

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
   public void Land2_Dequeue(int present_time)//ù��° ���� ť�� ���� Dequeue
   {
      if (l2_Q.size()!=0&&l2_ind<l2_Q.size())
      {
         l2_Q.get(l2_ind).SetDeQ(present_time);//���� �ð��� ��� �ð� ����
         l2_waitTotal+=l2_Q.get(l2_ind).GetWTime();//��� ��� �ð��� ��� �ð� �߰�
         l2_remTimeTotal+=l2_Q.get(l2_ind).GetRemainTime();//��� ���� ���� �ð��� �߰�

         if (l2_Q.get(l2_ind).GetEmergencyStatus()==true)//��� ���� ������̸�
         {
            l2_deq_status="�ڡڡ�"+l2_Q.get(l2_ind).GetNumber()+" ����Ⱑ ��� ������\n";
            l2emer_count++;//��� ���� ī��Ʈ ������Ŵ
         }
         else//�Ϲ� ���� ������̸�
            l2_deq_status=l2_Q.get(l2_ind).GetNumber()+" ����Ⱑ ������\n";

         l2_ind++;//���� �ε����� �̵�
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
   //��� ������ �õ��ؾ� �ϴ� ����Ⱑ �ִ���, �׸��� ��� ����Ⱑ �ִ��� �˻��ϴ� �Լ�
   public void Check_LandQ1()
   {
      int emer_ind1=-1;//��� ������ �ʿ��� ������� �ε���(1��° ���� ť)
      int acc_count=0;//�ش� ���� �ð��� �� ���� ��� ����Ⱑ �߻��Ͽ��°��� Ȯ����
      Emergency=new Airplane(-1,2);
      if (l1_Q.size()!=0)//ť�� ������� ������
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            if (l1_Q.get(i).GetRemainTime()==1)
            {//���� ��� ������ �ʿ��� ����Ⱑ �ִٸ�
               l1_return_emer_count++;
               emer_ind1=i;//�ش� �ε����� �����ְ�
               break;//Ż��
            }
         }
      }
      if (l1_Q.size()!=0)//ť�� ������� ������
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            if (l1_Q.get(i).GetFuel()<2)//���ᰡ �� ������ ����Ⱑ ť�� ����������?
            {//����� ��...
               l1_Q.remove(i);
               accident_count++;
               l1_acc_count++;
               acc_count++;
            }   //���ᷮ�� -�� ������ �ٷ� �� �ڸ����� ī��Ʈ ©�󳻰� ��� ó���ع�����.
         }
      }
      if (acc_count!=0)
      {

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            l1_acc_status="��� ����Ⱑ "+acc_count+"�� �߻���\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_acc_status);
            this.land_ta1.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            l1_acc_status="��� ����Ⱑ "+acc_count+"�� �߻���\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_acc_status);
            this.land_ta1.append(old);
         }
      }
      if (emer_ind1!=-1&&emer_ind1<l1_Q.size())
      {
         Emergency=l1_Q.get(emer_ind1);
         Emergency.SetEmerPriority();//�켱 ���� �����ϰ�
         Emergency.SetEmergency();//����ϴٰ� �ٲ�

         if(Thread.currentThread() == Depart_Land.first_th) // ���߻��� 1�� Ȱ�ַ� �����忡�� �Ͼ�ٸ�
         {
            l1_emer_status="��� ���� ����Ⱑ �߻���\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_emer_status);
            this.land_ta1.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,1.1); // ��� ���� ����⸦ ����
         }
         if(Thread.currentThread() == Depart_Land.second_th)// ���߻��� 2�� Ȱ�ַ� �����忡�� �Ͼ�ٸ�
         {
            l1_emer_status="��� ���� ����Ⱑ �߻���\n";
            String old=   this.land_ta1.getText();
            this.land_ta1.setText("");
            this.land_ta1.append(l1_emer_status);
            this.land_ta1.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,2.1); // ��� ���� ����⸦ ����
         }

         l1_Q.remove(emer_ind1);//���� �� �ش� ������ ����
      }      
   }
   public synchronized void Check_LandQ2()
   {
      int emer_ind2=-1;
      int acc_count=0;//�ش� ���� �ð��� �� ���� ��� ����Ⱑ �߻��Ͽ��°��� Ȯ����
      Airplane Emergency=new Airplane(-1,2);
      if (l2_Q.size()!=0)
      {
         for (int i=l2_ind; i<l2_Q.size(); i++)
         {
            if (l2_Q.get(i).GetRemainTime()==2)
            {//���� ��� ������ �ʿ��� ����Ⱑ �ִٸ�
               emer_ind2=i;//�ش� �ε����� �����ְ�
               l2_return_emer_count++;//ť ������ ������ ��� ���� ����� Ƚ�� ����
               break;//Ż��
            }
         }
      }
      if (l2_Q.size()!=0)//ť�� ������� ������
      {
         for (int i=l2_ind; i<l2_Q.size(); i++)
         {
            if (l2_Q.get(i).GetFuel()<2)//���ᰡ �� ������ ����Ⱑ ť�� ����������?
            {//����� ��...
               l2_Q.remove(i);
               accident_count++;
               l2_acc_count++;
               acc_count++;
            }   //���ᷮ�� -�� ������ �ٷ� �� �ڸ����� ©�󳻰� ��� ó���ع�����.
         }
      }
      if (acc_count!=0)
      {

         if(Thread.currentThread() == Depart_Land.first_th)
         {
            l2_acc_status="��� ����Ⱑ "+acc_count+"�� �߻���\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_acc_status);
            this.land_ta2.append(old);
         }
         else if(Thread.currentThread() == Depart_Land.second_th)
         {
            l2_acc_status="��� ����Ⱑ "+acc_count+"�� �߻���\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_acc_status);
            this.land_ta2.append(old);
         }
      }
      //���� ���޹��� ������� Enqueue �ð��� �˻��Ͽ� ��� ������ �õ��ϴ� ����Ⱑ
      //�ִ��� �������� �˻�(-1�̸� ��� ������ �õ��ϴ� ����Ⱑ �������� �ǹ�)
      if (emer_ind2!=-1&&emer_ind2<l2_Q.size())
      {
         Emergency=l2_Q.get(emer_ind2);
         Emergency.SetEmerPriority();//�켱 ���� �����ϰ�
         Emergency.SetEmergency();//��� �������� �����ϰ�

         if(Thread.currentThread() == Depart_Land.first_th) // ���߻��� 1�� Ȱ�ַν����忡�� �Ͼ�ٸ�
         {
            l2_emer_status="��� ���� ����Ⱑ �߻���\n";
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_emer_status);
            this.land_ta2.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,1.2); // ��� ���� ����⸦ �������ְ�
         }
         if(Thread.currentThread() == Depart_Land.second_th)// ���߻��� 2�� Ȱ�ַν����忡�� �Ͼ�ٸ�
         {
            String old=   this.land_ta2.getText();
            this.land_ta2.setText("");
            this.land_ta2.append(l2_emer_status);
            this.land_ta2.append(old);
            ShareAirplane.SetEmrAirplane(Emergency,2.2); // ��� ���� ����⸦ �������ְ�
         }

         l2_Q.remove(emer_ind2);//��� �����ؾߵǴϱ� ���� ť���� ������
      }


   }
   //***********************************************
   //�� ť�� ����� ����ϰ� �����ؾ� �Ѵٰ� �����Ƿ�
   //ť�� ���̸� �ùķ����ͷ� �����ϴ� �Լ��� �ʿ�
   public int SizeofDefQ()//�̷� ť�� ����� ��ȯ�ϴ� �Լ�
   {
      if (d_Q.size()!=0)
         return d_Q.size();//ť�� �� ���� ����� �̻��� ���� ���� �ִٸ�
      else//ť�� �� ���� ��������� ���� ���� ���ٸ�
         return 0;
   }
   public int SizeofLandQ_1()//ù��° ���� ť
   {
      if (l1_Q.size()!=0)//ť�� �� ���� ����� �̻��� ���� ���� �ִٸ�
         return l1_Q.size();
      else//ť�� �� ���� ��������� ���� ���� ���ٸ�
         return 0;
   }
   public int SizeofLandQ_2()//�ι�° ���� ť�� ����� ��ȯ�ϴ� �Լ�
   {
      if(l2_Q.size()!=0)//ť�� �� ���� ����� �̻��� ���� ���� �ִٸ�
         return l2_Q.size();
      else//ť�� �� ���� ��������� ���� ���� ���ٸ�
         return 0;
   }
   public int LandQSizeaver()//(a+b)/2�� �̸� ����س���
   {
      return (l1_Q.size()+l2_Q.size())/2;
   }
   //***********************************************
   //���� ť�� ���ᷮ�� ���� ���� �ð� ����
   public void Land1_renewal()
   {
      if (l1_Q.size()!=0)//ť�� ������� �ʴٸ�
      {
         for (int i=l1_ind; i<l1_Q.size(); i++)
         {
            l1_Q.get(i).ReduceFuel();//���ᷮ 2����
            l1_Q.get(i).OperationRemainTime();//���ҽ�Ų ���ᷮ ���� ���� �ð� ���
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
   //��� �ð� ��ȯ
   //�� Ȱ�ַο� ���ؼ� ����� ����ؾ� �ϹǷ� �ϳ��� Ȱ�ַ� ������ ����Ͽ� ��ȯ�ϸ� �� ��
   //(x/x')+(y/y')+(z/z')�� �ƴ϶� (x+y+z)/(x'+y'+z')�� ����ؾ� ��
   public int GetDepWtime()//�̷��� �������� �̷� ��� �ð� ��ȯ
   {
      return d_waitTotal;
   }
   public int GetLandWtime()//Ȱ�ַο� ������ �������� ���� ��� �ð� ��ȯ
   {
      return l1_waitTotal+l2_waitTotal;
   }
   public int GetLandRtime()//Ȱ�ַο� ������ �������� ���� ���� ���� �ð� ��ȯ
   {
      return l1_remTimeTotal+l2_remTimeTotal;
   }
   public int GetLandIndex()//���� ť�� ���� �ε����� ��ȯ
   //��� �ð��� ����ϱ� ���ؼ��� �ʿ�������
   {//���� ��� �� ��� ���� ����� ������ ����� ������ �ʿ�
      return l1_ind+l2_ind;
   }
   //***********************************************
   //��� �������� 4���� ����Ⱑ ������ �� �ϳ� ©�󳻾� �ϹǷ� ��� ī��Ʈ�� �ܺο��� ������ų ���𰡰� �ʿ�
   //�̶� ���� ó���Ǵ� ������ 2��° Ȱ�ַ��� 2�� ���� ť�̹Ƿ� 2��° ���� ť�� ��� ī��Ʈ�� ������ų �Լ��� ������ ��
   public void IncrL2AccCount()//2��° ���� ť�� ��� ī��Ʈ 1 ����
   {
      l2_acc_count++;
   }
   //***********************************************
   public void Setl1EmerEnqueue()//1�� ���� ť�� ��� ���� ����Ⱑ ����
   {
      l1_isEmerEnqueue=true;
   }
   public void Setl2EmerEnqueue()//1�� ���� ť�� ��� ���� ����Ⱑ ����
   {
      l2_isEmerEnqueue=true;
   }
   //***********************************************
   //�� �ܿ� ī��Ʈ ������ ��ȯ
   public int GetDepEnqeueCount()//�̷� ť�� ����(�̷� ���� ��� ����) ����� �� ��ȯ
   {
      return d_count;
   }
   public int GetL1EmerDequeueCount()//1��° ���� ť���� ��� ������ ����� ���� ��ȯ
   {
      return l1emer_count;
   }
   public int GetL2EmerDequeueCount()//2���� ���� ť���� ��� ������ ����� ���� ��ȯ
   {
      return l2emer_count;
   }
   public int GetEmerDequeueCount()//�ش� Ȱ�ַο��� ��� ������ ��� ����� ���� ��ȯ
   {
      return l1emer_count+l2emer_count;
   }
   public int GetAccidentCount()//��� �Ͼ ����� ���� ��ȯ
   {
      return accident_count;
   }
   public int L1NotDequeuedCount()//1��° ���� ť�� ���� �ִ� ����� �� ��ȯ
   {
      return l1_Q.size()-l1_ind;
   }
   public int L2NotDequeuedCount()//2��° ���� ť�� ���� �ִ� ����� �� ��ȯ
   {
      return l2_Q.size()-l2_ind;
   }
   public int GetLand1EnqueueCount()//1�� ���� ť�� ���� ��� ����� �� ��ȯ
   {
      return l1_return_emer_count+l1_acc_count+(l1_Q.size()-l1_ind)-l1emer_count+l1_ind;
   }
   public int GetLand2EnqueueCount()//2�� ���� ť�� ���� ��� ����� �� ��ȯ
   {
      return l2_return_emer_count+l2_acc_count+(l2_Q.size()-l2_ind)-l2emer_count+l2_ind;
   }
   public int GetL1ReturnEmerCount()//1�� ���� ť���� ��� ������ ���� ���� ������� ī��Ʈ ��ȯ
   {
      return l1_return_emer_count;
   }
   public int GetL2ReturnEmerCount()//2�� ���� ť���� ��� ������ ���� ���� ������� ī��Ʈ ��ȯ
   {
      return l2_return_emer_count;
   }
   public int GetL1AccCount()//1�� ���� ť�� ��� ī��Ʈ ��ȯ
   {
      return l1_acc_count;
   }
   public int GetL2AccCount()//2�� ���� ť�� ��� ī��Ʈ ��ȯ
   {
      return l2_acc_count;
   }
   //�ε��� ��ȯ
   //��� ��¿� �ʿ�
   public int GetLand1Index()//1��° ���� ť�� ���� �ε��� ��ȯ
   {
      return l1_ind;
   }
   public int GetLand2Index()//2��° ���� ť�� ���� �ε��� ��ȯ
   {
      return l2_ind;
   }
   public int GetDepIndex()//�̷� ť�� ���� �ε��� ��ȯ(�̷��� ����� ��)
   {
      return d_ind;
   }
   public int Get1stThreadTime()//1�� Ȱ�ַ� �������� ���� �ð� ��ȯ
   {
      return first_thread_Time;
   }
   public int Get2ndThreadTime()//2�� Ȱ�ַ� �������� ���� �ð� ��ȯ
   {
      return second_thread_Time;
   }
}