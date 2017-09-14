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
   {//세 개의 활주로에 대해 통계를 다 모으고 다시 계산해야 하기 때문에 매개 변수로
      //각 활주로 객체를 받아옴
      firstRunway=first;
      secondRunway=second;
      thirdRunway=third;
      acci_percent=0.0;
      emer_percent=0.0;
       nf = NumberFormat.getInstance();
   }
   public String MiddleStat()
   {//중간 결과 출력
      /*중간 결과는
      - 각 큐의 내용
      - 평균 이륙 대기 시간
      - 평균 착륙 대기 시간
      - 착륙 시 남아 있는 제한 시간의 평균
       */
      //착륙한 비행기의 평균 비행 가능 시간 계산
      double dep_waitaver=(double)(firstRunway.GetDepWtime()+secondRunway.GetDepWtime()+
            thirdRunway.GetDepWtime())/(firstRunway.GetDepIndex()+
                  secondRunway.GetDepIndex()+thirdRunway.GetRealDepCount());
      //평균 착륙 대기 시간 계산
      double land_waitaver=(double)(firstRunway.GetLandWtime()+secondRunway.GetLandWtime()+thirdRunway.GetEmerLandWtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount());
      //착륙한 비행기의 평균 비행 가능 시간 계산
      double land_rtimeaver=(double)(firstRunway.GetLandRtime()+secondRunway.GetLandRtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex());
      
      
      String first="★1번 활주로\n";
      String firstdep_enqcount="- 이륙을 시도한 비행기 수 : "
            +firstRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로의 이륙 큐에 Enqueue된 이륙 비행기 수(이륙 유무 상관 x)
      String firstdep_deqcount="- 총 이륙한 비행기 수 : "+firstRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로에서 이륙한 비행기 수(긴급 착륙, 이륙 대기 제외)
      String firstland1_enqcount="- 1번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +firstRunway.GetLand1EnqueueCount()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String firstland1_deqcount="- 1번 착륙 큐에 착륙한 비행기 수 : "+firstRunway.GetLand1Index()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 착륙한 비행기 수(긴급 착륙, 사고 제외)
      String firstland2_inqcount="- 2번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +firstRunway.GetLand2EnqueueCount()+" 대\n";
      //1번 활주로의 2번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String firstland2_deqcount="- 2번 착륙 큐에 착륙한 비행기 수 : "+firstRunway.GetLand2Index()+" 대\n";

      String second="★2번 활주로\n";
      String seconddep_enqcount="- 이륙을 시도한 비행기 수 : "
            +secondRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로의 이륙 큐에 Enqueue된 이륙 비행기 수(이륙 유무 상관 x)
      String seconddep_deqcount="- 총 이륙한 비행기 수 : "+secondRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로에서 이륙한 비행기 수(긴급 착륙, 이륙 대기 제외)
      String secondland1_enqcount="- 1번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +secondRunway.GetLand1EnqueueCount()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String secondland1_deqcount="- 1번 착륙 큐에 착륙한 비행기 수 : "+secondRunway.GetLand1Index()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 착륙한 비행기 수(긴급 착륙, 사고 제외)
      String secondland2_inqcount="- 2번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +secondRunway.GetLand2EnqueueCount()+" 대\n";
      //1번 활주로의 2번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String secondland2_deqcount="-2번 착륙 큐에 착륙한 비행기 수 : "+secondRunway.GetLand2Index()+" 대\n";
      //1번 활주로의 2번 착륙 큐에 착륙한 비행기 수(긴급 착륙, 사고 제외)
      
      String third="★3번 활주로\n";
      String thirddep_enqcount="- 이륙을 시도한 비행기 수 : "+thirdRunway.GetDepCount()+" 대\n";
      String thirddep_emerenqcount="- 긴급 착륙한 비행기 수 : "+thirdRunway.GetEmergencyCount()+" 대\n";
      String thirddep_deqcount="- 총 이륙한 비행기 수 : "+thirdRunway.Get3rdIndex()+" 대\n";
      
      String seperator="---------------------------\n";
      String dep_average="- 평균 이륙 대기 시간 : "+nf.format(dep_waitaver)+" 분\n";
      String land_average="- 평균 착륙 대기 시간 : "+nf.format(land_waitaver)+" 분\n";
      String remain_average="- 착륙한 비행기들의 평균 비행 가능 시간 : "+nf.format(land_rtimeaver)+" 분\n\n";
      return first+firstdep_enqcount+firstdep_deqcount+firstland1_enqcount+firstland1_deqcount
            +firstland2_inqcount+firstland2_deqcount+second+seconddep_enqcount+seconddep_deqcount
            +secondland1_enqcount+secondland1_deqcount+secondland2_inqcount+secondland2_deqcount
            +third+thirddep_enqcount+thirddep_emerenqcount+thirddep_deqcount+seperator+dep_average+land_average+remain_average;
      //이거 한꺼번에 반환받아서 TextArea에다가 붙임
   }
   public String FinalStat()
   {//최종 결과 출력
      //평균 이륙 대기 시간 계산
      double dep_waitaver=(double)(firstRunway.GetDepWtime()+secondRunway.GetDepWtime()+
            thirdRunway.GetDepWtime())/(firstRunway.GetDepIndex()+
                  secondRunway.GetDepIndex()+thirdRunway.GetRealDepCount());
      //평균 착륙 대기 시간 계산
      double land_waitaver=(double)(firstRunway.GetLandWtime()+secondRunway.GetLandWtime()+thirdRunway.GetEmerLandWtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount());
      //착륙한 비행기의 평균 비행 가능 시간 계산
      double land_rtimeaver=(double)(firstRunway.GetLandRtime()+secondRunway.GetLandRtime())
            /(firstRunway.GetLandIndex()+secondRunway.GetLandIndex());
      String first="★1번 활주로\n";
      String firstdep_enqcount="- 이륙을 시도한 비행기 수 : "
            +firstRunway.GetDepEnqeueCount()+" 대 /";
      //1번 활주로의 이륙 큐에 Enqueue된 이륙 비행기 수(이륙 유무 상관 x)
      String firstdep_deqcount="- 총 이륙한 비행기 수 : "+firstRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로에서 이륙한 비행기 수(긴급 착륙, 이륙 대기 제외)
      
      String firstland1_enqcount="- 1번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +firstRunway.GetLand1EnqueueCount()+" 대 /";
      String firstland1_emercount="- 1번 착륙 큐에서 빠져나간 긴급 착륙 비행기 수 : "+firstRunway.GetL1ReturnEmerCount()+"대\n";
      String firstland1_acccount="- 1번 착륙 큐에서 발생한 사고 비행기 수 : "+firstRunway.GetL1AccCount()+" 대 /";
      String firstland1_emerenqcount="- 1번 착륙 큐로 긴급 착륙한 비행기 수 : "+firstRunway.GetL1EmerDequeueCount()+" 대\n";
      String firstland1_notdeqcount="- 1번 착류 큐에 남아있는 비행기 수 : "+firstRunway.L1NotDequeuedCount()+"대 /";
      //1번 활주로의 1번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String firstland1_deqcount="- 1번 착륙 큐에 착륙한 비행기 수 : "+firstRunway.GetLand1Index()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 착륙한 비행기 수(긴급 착륙, 사고 제외)
      
      String firstland2_inqcount="- 2번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +firstRunway.GetLand2EnqueueCount()+" 대 /";
      String firstland2_emercount="- 2번 착륙 큐에서 빠져나간 긴급 착륙 비행기 수 : "+firstRunway.GetL2ReturnEmerCount()+"대\n";
      String firstland2_acccount="- 2번 착륙 큐에서 발생한 사고 비행기 수 : "+firstRunway.GetL2AccCount()+" 대 /";
      String firstland2_emerenqcount="- 2번 착륙 큐로 긴급 착륙한 비행기 수 : "+firstRunway.GetL2EmerDequeueCount()+" 대\n";
      String firstland2_notdeqcount="- 2번 착류 큐에 남아있는 비행기 수 : "+firstRunway.L2NotDequeuedCount()+"대 /";
      //1번 활주로의 2번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String firstland2_deqcount="- 2번 착륙 큐에 착륙한 비행기 수 : "+firstRunway.GetLand2Index()+" 대\n";

      String second="★2번 활주로\n";
      String seconddep_enqcount="- 이륙을 시도한 비행기 수 : "
            +secondRunway.GetDepEnqeueCount()+" 대 /";
      //1번 활주로의 이륙 큐에 Enqueue된 이륙 비행기 수(이륙 유무 상관 x)
      String seconddep_deqcount="- 총 이륙한 비행기 수 : "+secondRunway.GetDepEnqeueCount()+" 대\n";
      //1번 활주로에서 이륙한 비행기 수(긴급 착륙, 이륙 대기 제외)
      String secondland1_enqcount="- 1번 착륙 큐에서 착륙을 시도한 비행기 수 : "
            +secondRunway.GetLand1EnqueueCount()+" 대 /";
      String secondland1_emercount="- 1번 착륙 큐에서 빠져나간 긴급 착륙 비행기 수 : "+secondRunway.GetL1ReturnEmerCount()+"대\n";
      String secondland1_acccount="- 1번 착륙 큐에서 발생한 사고 비행기 수 : "+secondRunway.GetL1AccCount()+" 대 /";
      String secondland1_emerenqcount="- 1번 착륙 큐로 긴급 착륙한 비행기 수 : "+secondRunway.GetL1EmerDequeueCount()+" 대\n";
      String secondland1_notdeqcount="- 1번 착류 큐에 남아있는 비행기 수 : "+secondRunway.L1NotDequeuedCount()+"대 /";
      //1번 활주로의 1번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String secondland1_deqcount="- 1번 착륙 큐에 착륙한 비행기 수 : "+secondRunway.GetLand1Index()+" 대\n";
      //1번 활주로의 1번 착륙 큐에 착륙한 비행기 수(긴급 착륙, 사고 제외)
      
      
      
      String secondland2_inqcount="- 2번 착륙 큐에서 착륙을 시도한 비행기 수 : "
               +secondRunway.GetLand2EnqueueCount()+" 대 /";
      String secondland2_emercount="- 2번 착륙 큐에서 빠져나간 긴급 착륙 비행기 수 : "+secondRunway.GetL2ReturnEmerCount()+"대\n";
      String secondland2_acccount="- 2번 착륙 큐에서 발생한 사고 비행기 수 : "+secondRunway.GetL2AccCount()+" 대 /";
      String secondland2_emerenqcount="- 2번 착륙 큐로 긴급 착륙한 비행기 수 : "+secondRunway.GetL2EmerDequeueCount()+" 대\n";
      String secondland2_notdeqcount="- 2번 착류 큐에 남아있는 비행기 수 : "+secondRunway.L2NotDequeuedCount()+"대 /";
      //1번 활주로의 2번 착륙 큐에 들어온 비행기 수(긴급 착륙, 사고 여부 상관 x)
      String secondland2_deqcount="- 2번 착륙 큐에 착륙한 비행기 수 : "+secondRunway.GetLand2Index()+" 대\n";

      
      
      String third="★3번 활주로 \n";
      String thirddep_enqcount="- 이륙을 시도한 비행기 수 : "+thirdRunway.GetDepCount()+" 대 /";
      String thirddep_emerenqcount="- 긴급 착륙한 비행기 수 : "+thirdRunway.GetEmergencyCount()+" 대\n";
      String thirddep_notdeqcount="- 큐에 남아있는 비행기 수 : "+thirdRunway.NotDequeueCount()+"대 /";
      String thirddep_deqcount="- 총 이륙한 비행기 수 : "+thirdRunway.Get3rdIndex()+" 대\n\n";

      String seperator="---------------------------\n";
      String dep_average="- 평균 이륙 대기 시간 : "+nf.format(dep_waitaver)+" 분\n";
      String land_average="- 평균 착륙 대기 시간 : "+nf.format(land_waitaver)+" 분\n";
      String remain_average="- 착륙한 비행기들의 평균 비행 가능 시간 : "+nf.format(land_rtimeaver)+" 분\n\n";
      
      //긴급 착륙 비행기의 수 : 1,2,3번 활주로의 이륙 큐에 들어온 긴급 착륙 비행기 수의 합
      int emer_count=(firstRunway.GetEmerDequeueCount()+secondRunway.GetEmerDequeueCount()+
            thirdRunway.GetEmergencyCount());
      //비율 : 긴급 착륙 비행기 수/전체 착륙 비행기 수(착륙 큐의 인덱스들)
      emer_percent=((double)emer_count/(firstRunway.GetLandIndex()+secondRunway.GetLandIndex()+thirdRunway.GetEmergencyCount()))*100;
      
      //사고 비행기 수 : 1,2번 활주로의 사고 비행기 카운트의 합
      int acci_count=(firstRunway.GetAccidentCount()+secondRunway.GetAccidentCount());
      //비율 : 사고 비행기수/전체 착륙 비행기 수(착륙 큐의 인덱스들+사고 비행기 수)
      acci_percent=((double)acci_count/(acci_count+firstRunway.GetLandIndex()+secondRunway.GetLandIndex()))*100;
      String emergency="- 긴급 착륙을 시행한 비행기의 수와 비율 : \n"+emer_count+" 대, "+nf.format(emer_percent)+"%\n";
      String accident="- 사고가 발생한 비행기의 수와 비율 : \n"+acci_count+" 대, "+nf.format(acci_percent)+"%";
      
      return first+firstdep_enqcount+firstdep_deqcount+firstland1_enqcount+
            firstland1_emercount+firstland1_acccount+firstland1_emerenqcount+firstland1_notdeqcount+firstland1_deqcount
            +firstland2_inqcount+firstland2_emercount+firstland2_acccount+firstland2_emerenqcount+firstland2_notdeqcount+firstland2_deqcount+second+seconddep_enqcount+seconddep_deqcount
            +secondland1_enqcount+secondland1_emercount+secondland1_acccount+secondland1_emerenqcount+secondland1_notdeqcount+secondland1_deqcount+secondland2_inqcount+secondland2_emercount+
            secondland2_acccount+secondland2_emerenqcount+secondland2_notdeqcount+secondland2_deqcount
            +third+thirddep_enqcount+thirddep_emerenqcount+thirddep_notdeqcount+thirddep_deqcount+seperator+dep_average+land_average+
            remain_average+emergency+accident;
   }
}