package com.swyp.wedding.entity.dress;

public class DressEnum {
    
    // 시즌 Enum
    public enum Season {
        SPRING_SUMMER("여름"),
        FALL_WINTER("겨울"),
        SPRING_FALL("봄/가을"),
        ALL_SEASON("사계절");
        
        private final String koreanName;
        
        Season(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 드레스 타입 Enum
    public enum Type {
        A_LINE("A라인"),
        MERMAID("머메이드"),
        BALL_LINE("벨라인"),
        SLIP_SHEATH("슬립/시스라인"),
        JUMPSUIT("점프슈트"),
        PRINCESS("프린세스"),
        EMPIRE("엠파이어");
        
        private final String koreanName;
        
        Type(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 네크라인 Enum
    public enum Neckline {
        V_NECK("V넥"),
        ROUND_NECK("라운드넥"),
        SQUARE_NECK("스퀘어넥"),
        OFF_SHOULDER("오프숄더"),
        HALTER("홀터"),
        HALTER_NECK("홀터넥"),
        HIGH_NECK("하이넥"),
        BOAT_NECK("보트넥"),
        ONE_SHOULDER("원숄더"),
        TUBE_TOP("튜브탑"),
        SLEEVELESS("나시");
        
        private final String koreanName;
        
        Neckline(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 무드 Enum
    public enum Mood {
        ROMANTIC("로맨틱"),
        MODERN("모던"),
        MODERN_CHIC("모던 시크"),
        SEXY_ELEGANT("섹시 우아"),
        ELEGANT("우아함"),
        SIMPLE_CHIC("심플 시크"),
        SIMPLE_ELEGANT("심플 우아"),
        PREMIUM_LUXURY("프리미엄 럭셔리"),
        GLAMOROUS("화려한"),
        SIMPLE("심플"),
        LUXURY("럭셔리"),
        LOVELY("사랑스러운"),
        BOHEMIAN("보헤미안");
        
        private final String koreanName;
        
        Mood(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 드레스 길이 Enum
    public enum Length {
        LONG("롱 드레스"),
        MIDI("미디"),
        SHORT("숏");
        
        private final String koreanName;
        
        Length(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 원단 타입 Enum
    public enum FabricType {
        PEARL("펄"),
        LACE("레이스"),
        CREPE("크레이프"),
        FLOWER("플라워"),
        BEADS("비즈"),
        SEQUINS("스팽글"),
        VINTAGE("빈티지"),
        SILK("실크"),
        RIBBON("리본"),
        SATIN("새틴"),
        CHIFFON("시폰"),
        TULLE("튤");
        
        private final String koreanName;
        
        FabricType(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
    
    // 드레스 용도 Enum
    public enum Usage {
        MAIN_CEREMONY("본식"),
        SECOND_PARTY("2부 드레스"),
        PHOTO_SHOOT("촬영용"),
        SELF_WEDDING("셀프웨딩"),
        SMALL_WEDDING("스몰웨딩"),
        RECEPTION("리셉션"),
        PARTY("파티"),
        HOTEL_WEDDING("호텔웨딩");
        
        private final String koreanName;
        
        Usage(String koreanName) {
            this.koreanName = koreanName;
        }
        
        public String getKoreanName() {
            return koreanName;
        }
    }
}