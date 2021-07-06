<h1>1주차(7/1 ~ 7/7) - 프로젝트 기본 계획 및 스프링과 JSP 틀 잡기</h1>
<h2>레이아웃 페이지</h2>
<h3><a href="https://kokochi66.github.io/TFM-twitch-follower-manager/layout/index.html">메인페이지</a></h3>
<p>
    기본적인 페이지는 부트스트랩 무료 탬플릿인 <a href="https://bootstrapmade.com/eterna-free-multipurpose-bootstrap-template/">Eterna</a> 페이지를 기본으로 하고, 상세 내용을 조정하여 구성하였습니다.
    페이지의 구현할 내용은 다음과 같습니다.
</p>
<img src="https://user-images.githubusercontent.com/61536109/124602214-97a2da80-dea3-11eb-9fa9-db10363baa6e.png" />
<img src="https://user-images.githubusercontent.com/61536109/124602219-98d40780-dea3-11eb-8d09-21d6ac77073c.png" />
<ol>
    <li>메인 타이틀 : 구성할 페이지의 메인 타이틀이 들어갈 상단 navbar 의 타이틀 부분입니다.</li>
    <li>메인 스와이퍼(swiper) : Swiper.js를 이용한 메인 화면의 Swiper화면으로 구성되어있다. 표시될 화면은 비로그인시에는 랜덤 라이브중인 스트리머의 방송 중인 화면이 나오고, 클릭시에 해당 스트리머의 방송 링크로 이동하게 되며, 로그인시에는 구독한 스트리머 중, 시청자 순으로 방송화면을 표시하도록 합니다. 5개까지만 표시합니다.</li>
    <li>메뉴 설정 : 클릭 시, 메뉴가 나오고, 비로그인 시에는 로그인창이, 로그인 시에는 로그아웃과 기타 설정들이 표시되도록 합니다. 트위치 모양은 트위치 홈페이지로 이동하는 링크입니다.</li>
    <li>다시보기 찜목록 : 로그인시에는 사용자의 다시보기 찜 목록에 들어있는 영상들이 나타납니다. 만약 비로그인이거나, 다시보기 찜 목록에 들어있는 값이 없을 경우, 트위치 인기클립이 표시됩니다.</li>
    <li>방송 정렬 : 현재 라이브 스트리밍 중인 스트리머의 채널 중에서 각각 나의 팔로우한 영상, 시청자 순, 팔로워 순, 게임 별 정렬로 각 채널을 표시합니다. 표시되는 화면은 밑으로 내려가면 계속해서 더보기가 추가되면서 계속해서 8개씩 추가되는 구조로 구성할 예정입니다. (6번까지 포함)</li>
</ol><br>

<h3><a href="https://kokochi66.github.io/TFM-twitch-follower-manager/layout/login.html">로그인 페이지</a></h3>
<p>
    로그인 페이지는 일반 부트스트랩의 form 형태로 구성하였습니다.
</p>
<img src="https://user-images.githubusercontent.com/61536109/124603263-b3f34700-dea4-11eb-8166-128cf8883a40.png" />
<ol>
    <li>메인 타이틀 : 구성할 페이지의 메인 타이틀이 들어갈 상단 navbar 의 타이틀 부분입니다.</li>
</ol><br>

<h3><a href="https://kokochi66.github.io/TFM-twitch-follower-manager/layout/setting.html">설정 페이지</a></h3>
<p>
    설정 페이지에서는 프로필 설정이 가능합니다. 설정한 아이디와 비밀번호, 이메일 정보 변경하기와 자신의 계정과 연동한 트위치 아이디와 닉네임이 표시됩니다. 또한 계정 삭제하기 기능을 제공합니다.
</p>
<img src="https://user-images.githubusercontent.com/61536109/124603829-4c89c700-dea5-11eb-9a4c-722fa0526bdb.png" /><br>
<br>
<h3><a href="https://kokochi66.github.io/TFM-twitch-follower-manager/layout/follow.html">팔로우 채널 관리 페이지</a></h3>
<p>
    팔로우 채널 관리 페이지는 나의 연동된 트위치 계정의 팔로우 목록을 관리하는 페이지입니다. 이 창에서는 나의 팔로우 한 스트리머의 채널을 관리할 수 있으며, 새로운 스트리머를 추가하거나, 기존 스트리머를 삭제할 수 있습니다. 이외에도 구현 시에 여유가 남으면 해당 스트리머에 대한 상세한 정보와 로그인한 유저가 해당 스트리머를 언제부터 구독했는지, 도네이션을 얼마 했는지, 선호도가 어떻게 되는지 등등에 대한 정보도 추가할 예정입니다.
</p>
<img src="https://user-images.githubusercontent.com/61536109/124603414-e13ff500-dea4-11eb-88af-dd20ab2745e3.png" />
<ol>
    <li>각 스트리머를 정렬하는 select 입니다.</li>
    <li>버튼을 클릭하면 검색창으로 변경되어, 추가버튼을 누르면 다른 스트리머들을 검색하여 스트리머를 추가 팔로우할 수 있으며, 내 스트리머 검색을 클릭하면 내가 이미 팔로우 한 스트리머 내에서 스트리머를 검색할 수 있습니다.</li>
    <li>검색하여 정렬된 스트리머들입니다.</li>
</ol><br>

<h3><a href="https://kokochi66.github.io/TFM-twitch-follower-manager/layout/replay.html">다시보기 관리 페이지</a></h3>
<p>
    내가 찜한 다시보기를 표시해 줍니다. 트위치 API에서는 다시보기 찜 기능을 별도로 제공하지 않아서, 이 항목에 대해서는 별도의 데이터베이스를 구성하여 구현할 예정입니다. 편의성을 좀 더 올리기 위해서 여러가지 생각을 해봐야할 것 같습니다.
</p>
<img src="https://user-images.githubusercontent.com/61536109/124603416-e1d88b80-dea4-11eb-857d-907547fff291.png" />
<ol>
    <li>내가 원하는 트위치의 Video의 URL을 입력하면 해당 URL에 해당하는 다시보기 혹은 클립 동영상을 찜목록에 넣어줍니다. 폴더로 구성해서 관리할 수 있게 하는 방안에 대해서 고민하고 있습니다.</li>
    <li>나의 찜목록안에 들어있는 다시보기 영상들입니다. 표시될 영상에는 영상과 함께 해당 방송의 타이틀, 스트리머의 닉네임과 아이디, 방송일자, 방송시간 등의 정보가 표시되어야 하며, 버튼을 통해서 찜목록에 넣은 영상을 쉽게 삭제할 수 있도록 할 것이며, 우선순위를 구성하여 내가 우선적으로 보고싶은 다시보기를 정렬할 수 있도록 할 것입니다.</li>
</ol><br>