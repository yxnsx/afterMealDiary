# afterMealDiary
먹은 음식과 그에 대한 후기를 기록하고, 캘린더뷰를 통해 한눈에 볼 수 있는 기능을 제공합니다.</br>
구글 지도를 통해 사용자의 위치를 기반으로 주변의 음식점 정보도 제공할 수 있도록 했습니다.</br>
(영상: <https://vimeo.com/456448876>)
</br></br>


## 스크린샷
![afterMealDiary_screenShots 001](https://user-images.githubusercontent.com/47806943/92987948-366bde00-f502-11ea-9720-27d05191ac49.jpeg)
![afterMealDiary_screenShots 002](https://user-images.githubusercontent.com/47806943/92987951-3b309200-f502-11ea-9842-088b338263cb.jpeg)


## 라이브러리/APIs
* lottie (https://github.com/airbnb/lottie-android)</br>
* CompactCalendarView (https://github.com/SundeepK/CompactCalendarView)</br>
* Google Maps API
</br></br>


## 주요 기능
**1. 스플래시 화면**</br>
&nbsp;&nbsp;&nbsp; lottie를 이용하여 스플래시 애니메이션 화면을 구현했습니다.</br>

**2. 상단 배너**</br>
&nbsp;&nbsp;&nbsp; 3초마다 상단 배너가 바뀌며 각 배너마다 다른 활동으로 연결됩니다.</br>
- 네이버 '내 주변 맛집' 검색
- 설정 탭의 메뉴 룰렛
- 네이버 '집에서 간단한 요리' 검색

**3. 메뉴 룰렛**</br>
&nbsp;&nbsp;&nbsp; 총 7가지의 메뉴 중 랜덤하게 하나를 선택하여 사용자에게 메뉴를 제안합니다.</br>
&nbsp;&nbsp;&nbsp; 룰렛 멈추기 버튼을 누를 때마다 lottie 애니메이션이 재생됩니다.

**3. 글 작성, 수정, 삭제**</br>
&nbsp;&nbsp;&nbsp; 먹은 음식의 사진과 함께 글을 작성할 수 있으며, 작성한 글은 수정 삭제가 가능합니다.</br>
&nbsp;&nbsp;&nbsp; 작성된 글은 홈 화면의 리사이클러뷰를 통해 리스트 형태로 보여집니다.

**4. 캘린더**</br>
&nbsp;&nbsp;&nbsp; 캘린더 탭에서 상단 캘린더뷰를 통해 각 일자별 작성된 글의 개수를 볼 수 있습니다.</br>
&nbsp;&nbsp;&nbsp; 각 날짜를 클릭하면 그 날 작성한 글이 하단 리스트뷰에 보여지며 </br>
&nbsp;&nbsp;&nbsp; 각 글을 클릭하면 글 상세보기 페이지로 이동합니다.</br>

**5. 주변 음식점 지도**</br>
&nbsp;&nbsp;&nbsp; 지도 탭에서 현재 위치를 기반으로 주변의 음식점 정보를 볼 수 있습니다.</br>

**6. 식사 알람 설정하기**</br>
&nbsp;&nbsp;&nbsp; 설정 탭의 식사 알람 설정하기로 들어가면 식사 알람을 설정할 수 있습니다.</br>
&nbsp;&nbsp;&nbsp; 알람은 여러개를 설정할 수 있으며 하단 리스트뷰에서 롱클릭시 삭제가 가능합니다.</br>
&nbsp;&nbsp;&nbsp; 앱이 실행중이지 않을 때에도 알람이 울리며, 알람 시간이 되면 상단 노티피케이션과 함께 알림음이 울립니다.</br>
&nbsp;&nbsp;&nbsp; 노티피케이션을 터치하면 앱의 홈 화면으로 진입합니다.

**7. 식사 메이트 뽑기**</br>
&nbsp;&nbsp;&nbsp; 설정 탭의 식사 메이트 뽑기로 들어가면 식사 메이트를 뽑을 수 있습니다.</br>
&nbsp;&nbsp;&nbsp; 기기의 연락처 정보를 바탕으로 랜덤하게 식사 메이트를 뽑은 후</br>
&nbsp;&nbsp;&nbsp; 하단의 '문자로 연락하기' 버튼을 누르면 해당 연락처로 문자를 보낼 수 있습니다.</br>
&nbsp;&nbsp;&nbsp; 룰렛 멈추기 버튼을 누를 때마다 lottie 애니메이션이 재생됩니다.

**8. 의견 보내기**</br>
&nbsp;&nbsp;&nbsp; 의견 보내기 탭을 누르면 지정된 이메일 주소로 앱 사용에 대한 의견을 보낼 수 있습니다.
