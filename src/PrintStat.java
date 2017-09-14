package n_dep_n_land;
import java.text.NumberFormat;
public class PrintStat {
   private Runway_12 firstRunway;
   private Runway_12 secondRunway;
   private Runway_3 thirdRunway;
   private double acci_percent;
   private double emer_percent;
   private NumberFormat nf;
   public PrintStat(Runway_12 first, Runway_12 second, Runway_3 third)
   {//�� ���� Ȱ�ַο� ���� ��踦 �� ������ �ٽ� ����ؾ� �ϱ� ������ �Ű� ������
      //�� Ȱ�ַ� ��ü�� �޾ƿ�
      firstRunway=first;
      secondRunway=second;
      thirdRunway=third;
      acci_percent=0.0;
      emer_percent=0.0;
       nf = NumberFormat.getInstance();
   }
   public String MiddleStat()
   {//�߰� ��� ���
      /*�߰� �����
      - �� ť�� ����
      - ��� �̷� ��� �ð�
      - ��� ���� ��� �ð�
      - ���� �� ���� �ִ� ���� �ð��� ���
       */
      //������ ������� ��� ���� ���� �ð� ���
      double dep_waitaver=(double)(firstRunway.GetDepWtime()+secondRunway.GetDepWtime()+
            thirdRunway.GetDepWtime())/(firstRunway.GetDepIndex()+
                  secondRunway.GetDepIndex()+thirdRunway.GetRealDepCount());
      //��� ���� ��� �ð� ���
      double land_waitaver=(double)(firstRunway.GetLandWtime()+secondRunway.GetLandWtime()+thirdRunway.GetEmerLandWtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount());
      //������ ������� ��� ���� ���� �ð� ���
      double land_rtimeaver=(double)(firstRunway.GetLandRtime()+secondRunway.GetLandRtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex());
      
      
      String first="��1�� Ȱ�ַ�\n";
      String firstdep_enqcount="- �̷��� �õ��� ����� �� : "
            +firstRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַ��� �̷� ť�� Enqueue�� �̷� ����� ��(�̷� ���� ��� x)
      String firstdep_deqcount="- �� �̷��� ����� �� : "+firstRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַο��� �̷��� ����� ��(��� ����, �̷� ��� ����)
      String firstland1_enqcount="- 1�� ���� ť���� ������ �õ��� ����� �� : "
            +firstRunway.GetLand1EnqueueCount()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String firstland1_deqcount="- 1�� ���� ť�� ������ ����� �� : "+firstRunway.GetLand1Index()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ������ ����� ��(��� ����, ��� ����)
      String firstland2_inqcount="- 2�� ���� ť���� ������ �õ��� ����� �� : "
            +firstRunway.GetLand2EnqueueCount()+" ��\n";
      //1�� Ȱ�ַ��� 2�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String firstland2_deqcount="- 2�� ���� ť�� ������ ����� �� : "+firstRunway.GetLand2Index()+" ��\n";

      String second="��2�� Ȱ�ַ�\n";
      String seconddep_enqcount="- �̷��� �õ��� ����� �� : "
            +secondRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַ��� �̷� ť�� Enqueue�� �̷� ����� ��(�̷� ���� ��� x)
      String seconddep_deqcount="- �� �̷��� ����� �� : "+secondRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַο��� �̷��� ����� ��(��� ����, �̷� ��� ����)
      String secondland1_enqcount="- 1�� ���� ť���� ������ �õ��� ����� �� : "
            +secondRunway.GetLand1EnqueueCount()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String secondland1_deqcount="- 1�� ���� ť�� ������ ����� �� : "+secondRunway.GetLand1Index()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ������ ����� ��(��� ����, ��� ����)
      String secondland2_inqcount="- 2�� ���� ť���� ������ �õ��� ����� �� : "
            +secondRunway.GetLand2EnqueueCount()+" ��\n";
      //1�� Ȱ�ַ��� 2�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String secondland2_deqcount="-2�� ���� ť�� ������ ����� �� : "+secondRunway.GetLand2Index()+" ��\n";
      //1�� Ȱ�ַ��� 2�� ���� ť�� ������ ����� ��(��� ����, ��� ����)
      
      String third="��3�� Ȱ�ַ�\n";
      String thirddep_enqcount="- �̷��� �õ��� ����� �� : "+thirdRunway.GetDepCount()+" ��\n";
      String thirddep_emerenqcount="- ��� ������ ����� �� : "+thirdRunway.GetEmergencyCount()+" ��\n";
      String thirddep_deqcount="- �� �̷��� ����� �� : "+thirdRunway.Get3rdIndex()+" ��\n";
      
      String seperator="---------------------------\n";
      String dep_average="- ��� �̷� ��� �ð� : "+nf.format(dep_waitaver)+" ��\n";
      String land_average="- ��� ���� ��� �ð� : "+nf.format(land_waitaver)+" ��\n";
      String remain_average="- ������ �������� ��� ���� ���� �ð� : "+nf.format(land_rtimeaver)+" ��\n\n";
      return first+firstdep_enqcount+firstdep_deqcount+firstland1_enqcount+firstland1_deqcount
            +firstland2_inqcount+firstland2_deqcount+second+seconddep_enqcount+seconddep_deqcount
            +secondland1_enqcount+secondland1_deqcount+secondland2_inqcount+secondland2_deqcount
            +third+thirddep_enqcount+thirddep_emerenqcount+thirddep_deqcount+seperator+dep_average+land_average+remain_average;
      //�̰� �Ѳ����� ��ȯ�޾Ƽ� TextArea���ٰ� ����
   }
   public String FinalStat()
   {//���� ��� ���
      //��� �̷� ��� �ð� ���
      double dep_waitaver=(double)(firstRunway.GetDepWtime()+secondRunway.GetDepWtime()+
            thirdRunway.GetDepWtime())/(firstRunway.GetDepIndex()+
                  secondRunway.GetDepIndex()+thirdRunway.GetRealDepCount());
      //��� ���� ��� �ð� ���
      double land_waitaver=(double)(firstRunway.GetLandWtime()+secondRunway.GetLandWtime()+thirdRunway.GetEmerLandWtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount());
      //������ ������� ��� ���� ���� �ð� ���
      double land_rtimeaver=(double)(firstRunway.GetLandRtime()+secondRunway.GetLandRtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex());
      String first="��1�� Ȱ�ַ�\n";
      String firstdep_enqcount="- �̷��� �õ��� ����� �� : "
            +firstRunway.GetDepEnqeueCount()+" �� /";
      //1�� Ȱ�ַ��� �̷� ť�� Enqueue�� �̷� ����� ��(�̷� ���� ��� x)
      String firstdep_deqcount="- �� �̷��� ����� �� : "+firstRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַο��� �̷��� ����� ��(��� ����, �̷� ��� ����)
      
      String firstland1_enqcount="- 1�� ���� ť���� ������ �õ��� ����� �� : "
            +firstRunway.GetLand1EnqueueCount()+" �� /";
      String firstland1_emercount="- 1�� ���� ť���� �������� ��� ���� ����� �� : "+firstRunway.GetL1ReturnEmerCount()+"��\n";
      String firstland1_acccount="- 1�� ���� ť���� �߻��� ��� ����� �� : "+firstRunway.GetL1AccCount()+" �� /";
      String firstland1_emerenqcount="- 1�� ���� ť�� ��� ������ ����� �� : "+firstRunway.GetL1EmerDequeueCount()+" ��\n";
      String firstland1_notdeqcount="- 1�� ���� ť�� �����ִ� ����� �� : "+firstRunway.L1NotDequeuedCount()+"�� /";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String firstland1_deqcount="- 1�� ���� ť�� ������ ����� �� : "+firstRunway.GetLand1Index()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ������ ����� ��(��� ����, ��� ����)
      
      String firstland2_inqcount="- 2�� ���� ť���� ������ �õ��� ����� �� : "
            +firstRunway.GetLand2EnqueueCount()+" �� /";
      String firstland2_emercount="- 2�� ���� ť���� �������� ��� ���� ����� �� : "+firstRunway.GetL2ReturnEmerCount()+"��\n";
      String firstland2_acccount="- 2�� ���� ť���� �߻��� ��� ����� �� : "+firstRunway.GetL2AccCount()+" �� /";
      String firstland2_emerenqcount="- 2�� ���� ť�� ��� ������ ����� �� : "+firstRunway.GetL2EmerDequeueCount()+" ��\n";
      String firstland2_notdeqcount="- 2�� ���� ť�� �����ִ� ����� �� : "+firstRunway.L2NotDequeuedCount()+"�� /";
      //1�� Ȱ�ַ��� 2�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String firstland2_deqcount="- 2�� ���� ť�� ������ ����� �� : "+firstRunway.GetLand2Index()+" ��\n";

      String second="��2�� Ȱ�ַ�\n";
      String seconddep_enqcount="- �̷��� �õ��� ����� �� : "
            +secondRunway.GetDepEnqeueCount()+" �� /";
      //1�� Ȱ�ַ��� �̷� ť�� Enqueue�� �̷� ����� ��(�̷� ���� ��� x)
      String seconddep_deqcount="- �� �̷��� ����� �� : "+secondRunway.GetDepEnqeueCount()+" ��\n";
      //1�� Ȱ�ַο��� �̷��� ����� ��(��� ����, �̷� ��� ����)
      String secondland1_enqcount="- 1�� ���� ť���� ������ �õ��� ����� �� : "
            +secondRunway.GetLand1EnqueueCount()+" �� /";
      String secondland1_emercount="- 1�� ���� ť���� �������� ��� ���� ����� �� : "+secondRunway.GetL1ReturnEmerCount()+"��\n";
      String secondland1_acccount="- 1�� ���� ť���� �߻��� ��� ����� �� : "+secondRunway.GetL1AccCount()+" �� /";
      String secondland1_emerenqcount="- 1�� ���� ť�� ��� ������ ����� �� : "+secondRunway.GetL1EmerDequeueCount()+" ��\n";
      String secondland1_notdeqcount="- 1�� ���� ť�� �����ִ� ����� �� : "+secondRunway.L1NotDequeuedCount()+"�� /";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String secondland1_deqcount="- 1�� ���� ť�� ������ ����� �� : "+secondRunway.GetLand1Index()+" ��\n";
      //1�� Ȱ�ַ��� 1�� ���� ť�� ������ ����� ��(��� ����, ��� ����)
      
      
      
      String secondland2_inqcount="- 2�� ���� ť���� ������ �õ��� ����� �� : "
               +secondRunway.GetLand2EnqueueCount()+" �� /";
      String secondland2_emercount="- 2�� ���� ť���� �������� ��� ���� ����� �� : "+secondRunway.GetL2ReturnEmerCount()+"��\n";
      String secondland2_acccount="- 2�� ���� ť���� �߻��� ��� ����� �� : "+secondRunway.GetL2AccCount()+" �� /";
      String secondland2_emerenqcount="- 2�� ���� ť�� ��� ������ ����� �� : "+secondRunway.GetL2EmerDequeueCount()+" ��\n";
      String secondland2_notdeqcount="- 2�� ���� ť�� �����ִ� ����� �� : "+secondRunway.L2NotDequeuedCount()+"�� /";
      //1�� Ȱ�ַ��� 2�� ���� ť�� ���� ����� ��(��� ����, ��� ���� ��� x)
      String secondland2_deqcount="- 2�� ���� ť�� ������ ����� �� : "+secondRunway.GetLand2Index()+" ��\n";

      
      
      String third="��3�� Ȱ�ַ� \n";
      String thirddep_enqcount="- �̷��� �õ��� ����� �� : "+thirdRunway.GetDepCount()+" �� /";
      String thirddep_emerenqcount="- ��� ������ ����� �� : "+thirdRunway.GetEmergencyCount()+" ��\n";
      String thirddep_notdeqcount="- ť�� �����ִ� ����� �� : "+thirdRunway.NotDequeueCount()+"�� /";
      String thirddep_deqcount="- �� �̷��� ����� �� : "+thirdRunway.Get3rdIndex()+" ��\n\n";

      String seperator="---------------------------\n";
      String dep_average="- ��� �̷� ��� �ð� : "+nf.format(dep_waitaver)+" ��\n";
      String land_average="- ��� ���� ��� �ð� : "+nf.format(land_waitaver)+" ��\n";
      String remain_average="- ������ �������� ��� ���� ���� �ð� : "+nf.format(land_rtimeaver)+" ��\n\n";
      
      //��� ���� ������� �� : 1,2,3�� Ȱ�ַ��� �̷� ť�� ���� ��� ���� ����� ���� ��
      int emer_count=(firstRunway.GetEmerDequeueCount()+secondRunway.GetEmerDequeueCount()+
            thirdRunway.GetEmergencyCount());
      //���� : ��� ���� ����� ��/��ü ���� ����� ��(���� ť�� �ε�����)
      emer_percent=((double)emer_count/(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount()))*100;
      
      //��� ����� �� : 1,2�� Ȱ�ַ��� ��� ����� ī��Ʈ�� ��
      int acci_count=(firstRunway.GetAccidentCount()+secondRunway.GetAccidentCount());
      //���� : ��� ������/��ü ���� ����� ��(���� ť�� �ε�����+��� ����� ��)
      acci_percent=((double)acci_count/(acci_count+firstRunway.GetLandIndex()+secondRunway.GetLandIndex()))*100;
      String emergency="- ��� ������ ������ ������� ���� ���� : \n"+emer_count+" ��, "+nf.format(emer_percent)+"%\n";
      String accident="- ��� �߻��� ������� ���� ���� : \n"+acci_count+" ��, "+nf.format(acci_percent)+"%";
      
      return first+firstdep_enqcount+firstdep_deqcount+firstland1_enqcount+
            firstland1_emercount+firstland1_acccount+firstland1_emerenqcount+firstland1_notdeqcount+firstland1_deqcount
            +firstland2_inqcount+firstland2_emercount+firstland2_acccount+firstland2_emerenqcount+firstland2_notdeqcount+firstland2_deqcount+second+seconddep_enqcount+seconddep_deqcount
            +secondland1_enqcount+secondland1_emercount+secondland1_acccount+secondland1_emerenqcount+secondland1_notdeqcount+secondland1_deqcount+secondland2_inqcount+secondland2_emercount+
            secondland2_acccount+secondland2_emerenqcount+secondland2_notdeqcount+secondland2_deqcount
            +third+thirddep_enqcount+thirddep_emerenqcount+thirddep_notdeqcount+thirddep_deqcount+seperator+dep_average+land_average+
            remain_average+emergency+accident;
   }
}