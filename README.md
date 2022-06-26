# TripleTest

JAVA 8 /
Spring Boot 2.7.1 /
Data Jpa  /
MySql 5.7  

## DB Table 구성
저는 JPA를 활용하여 테이블 설계를 하였고 DB테이블을 따로 입력하여 구상한다면 이런 식으로 표현할 수 있을 것 같습니다.

```sql
create table Photo(
id bigint auto_increment primary key,
photoId varchar(50) not null,
review_id bigint null,
foreign key (review_id) references Review (id)
);

create table Place (
id bigint auto_increment primary key,
placeId varchar(50) unique not null
); create index i_place on Place (placeId);

create table Review(
id bigint auto_increment primary key,
reviewId varchar(50) unique not null,
userId varchar(50) not null,
content varchar(255) not null,
first bit not null,
PLACE bigint null,
foreign key (PLACE) references Place (id)
); create index i_review on Review (reviewId);

create table Mileage (
id bigint auto_increment primary key,
MEMBER bigint null,
place varchar(50) not null,
point int not null,
status varchar(25) not null,
type varchar(25) not null,
foreign key (MEMBER) references Members (id)
); 

create table Members (
id bigint auto_increment primary key,
userId varchar(50) unique not null,
totalPoint int not null
); create index i_member on member (userId);
```

## ERD.
![image](https://user-images.githubusercontent.com/95898434/175815356-5f239859-d7cb-4656-b3de-88e805c5612a.png)


서비스 특성상 회원들의 마일리지 증감 내역을 저장하고 있어야한다고 생각하였고, 
많은 내역들이 저장될 것이라 생각되어 마일리지 내역만을 담는 Mileage테이블을 따로 만들어주었습니다.
사용자가 한눈에 적립, 차감 내역을 확인하기 쉽도록 1점1점에 대해 데이터를 저장할 수 있도록하였습니다. 

Timestamped를 상속 받게 하여 등록시간을 기록합니다.



<br>

### POST /events
EventDto형태로 데이터를 담아서 /events 로 POST 요청이 오면, action값을 확인하여 각각 리뷰 등록, 수정, 삭제에 대한 로직이 실행됩니다.
정해진 액션(ADD , MOD, DELETE)가 아닐 경우 예외를 발생합니다.

* 리뷰 등록시
  * 입력받은 place의 reviewList가 비어있는지 확인하고 , 비어있으면 리뷰 첫 등록 보너스를 부여합니다. 
  * attachedPhotoIds(이미지 배열)가 비어있는지 확인하고, 값이 있다면 이미지 보너스를 부여합니다.
  * content가 비어있는지 확인하고, 비어있다면 예외를 발생합니다. 비어있지 않다면 등록 보너스를 부여합니다.
<br>

* 리뷰 수정시
  * content가 없다면 예외를 발생합니다.(텍스트 수정을 하지 않았을 수도 있지만, 수정할 시 문장을 아예 지울 수도 있다고 생각되어 내용이 없을 경우를 방지하고, 텍스트 수정이 없다면 이전에 작성된 내용이 넘어온다고 가정하였습니다.)
  * 기존에 이미지를 등록하였는지 확인합니다. 기존 review에 photos가 비어있는지 확인합니다.
    * 비어있을 경우
      전달 받은 데이터에 attachedPhotoIds가 들어있다면 이미지 보너스를 부여합니다.
    * 데이터가 들어있을 경우
      전달 받은 데이터에 attachedPhotoIds가 비어 있다면 이미지 보너스를 회수합니다. 

<br>

* 리뷰 삭제시 
  * 첫 등록이였는지 확인합니다. 
    리뷰가 저장이 될 때, 첫 리뷰일 경우 first 컬럼의 값을 true로 변경한다는 가정을 하였습니다.
    삭제 요청이 온 review의 first가 true면 첫 리뷰 보너스를 회수 합니다.
  * 이미지를 등록하였는지 확인합니다.
    해당 review의 photos 가 비어있는지 확인하고 , 비어있지 않다면 이미지 보너스를 회수합니다.
  * 리뷰 등록으로 부여받은 리뷰 보너스를 회수합니다.

<br>

### GET /points + {userId}

사용자의 현재 마일리지를 확인하려면,  /points + userId 로 GET요청을 보냅니다.
해당 사용자의 마일리지 총점을 반환해줍니다. 

