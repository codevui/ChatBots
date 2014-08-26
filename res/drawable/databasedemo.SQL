/********************/
/*                  */
/* databasedemo.SQL */
/*                  */
/********************/

/* Disable Foreign Keys */
pragma foreign_keys = off;
/* Begin Transaction */
begin transaction;

/* Database [team68] */
pragma auto_vacuum=0;
pragma encoding='UTF-8';
pragma page_size=1024;

/* Drop table [main].[Course] */
drop table if exists [main].[Course];

/* Table structure [main].[Course] */
CREATE TABLE [main].[Course] (
  [id] INT NOT NULL, 
  [numberSlide] INT NOT NULL, 
  [numberQuestion] INT NOT NULL, 
  [title] VARCHAR(255) NOT NULL, 
  CONSTRAINT [sqlite_autoindex_Course_1] PRIMARY KEY ([id]));

/* Data [main].[Course] */
insert into [main].[Course] values(1, 8, 3, 'Bài học về đèn tín hiệu giao thông');


/* Drop table [main].[Question] */
drop table if exists [main].[Question];

/* Table structure [main].[Question] */
CREATE TABLE [main].[Question] (
  [id] INT NOT NULL, 
  [id_course] INT NOT NULL REFERENCES [Course], 
  [type] INT NOT NULL, 
  [content] [VARCHAR ](255) NOT NULL, 
  [image] [VARCHAR ](255) NOT NULL, 
  CONSTRAINT [sqlite_autoindex_Question_1] PRIMARY KEY ([id]));

/* Data [main].[Question] */
insert into [main].[Question] values(1, 1, 1, 'Khi bạn đèn xanh bật sáng, thì các phương tiện sẽ làm gì?', 'denxanh1');
insert into [main].[Question] values(2, 1, 1, 'Khi bạn đèn đỏ bật sáng thì mọi người phải làm thế nào?', 'dendo1');
insert into [main].[Question] values(3, 1, 1, 'Bạn đèn vàng bật sáng thì xe đạp, xa máy và ô tô phải làm sao?', 'denvang1');


/* Drop table [main].[PlanAnswer] */
drop table if exists [main].[PlanAnswer];

/* Table structure [main].[PlanAnswer] */
CREATE TABLE [main].[PlanAnswer] (
  [id] INT NOT NULL, 
  [id_question] INT NOT NULL REFERENCES [Question], 
  [content] [VARCHAR ](255) NOT NULL, 
  [isTrue] BOOLEAN NOT NULL, 
  CONSTRAINT [sqlite_autoindex_PlanAnswer_1] PRIMARY KEY ([id]));

/* Data [main].[PlanAnswer] */
insert into [main].[PlanAnswer] values(1, 1, ' A: Tất cả oto xe máy, xe đạp đều phải dừng lại', 0);
insert into [main].[PlanAnswer] values(2, 1, 'B: Tất cả ô tô, xe máy, xe đạp đều được đi tiếp', 1);
insert into [main].[PlanAnswer] values(3, 2, 'A: Mọi người đều sẽ phải dừng lại', 1);
insert into [main].[PlanAnswer] values(4, 2, 'B: Tất cả mọi người đều được đi tiếp', 0);


/* Drop table [main].[Slide] */
drop table if exists [main].[Slide];

/* Table structure [main].[Slide] */
CREATE TABLE [main].[Slide] (
  [id] INT NOT NULL, 
  [idCourse] INT NOT NULL REFERENCES [Course], 
  [order] INT NOT NULL, 
  [image] [VARCHAR ](2000) NOT NULL, 
  [textPreview] [VARCHAR ](2000) NOT NULL, 
  [textToSay] [VARCHAR ](2000) NOT NULL, 
  CONSTRAINT [sqlite_autoindex_Slide_1] PRIMARY KEY ([id]));

/* Data [main].[Slide] */
insert into [main].[Slide] values(2, 1, 2, 'dentinhieu', 'Câu chuyện của bạn taxi về các đèn tín hiệu', 'Một hôm taxi gặp một ngã tư, đang đèn đỏ, nên tôi dừng lại và đã tình cờ nghe được cuộc tranh luận sôi nổi của các đèn tín hiệu giao thông');
insert into [main].[Slide] values(3, 1, 3, 'denxanh1', 'Chuyện của đèn xanh', 'Đầu tiên tôi nghe tiếng đèn xanh: Ở đây, tớ là người quan trọng nhất. Chỉ khi đèn xanh tớ sáng lên thì các phương tiện giao thông mới được đi. ');
insert into [main].[Slide] values(4, 1, 4, 'dendo1', 'Chuyện của đèn đỏ', 'Đèn xanh vừa dứt lời thì đèn đỏ cũng không chịu kém phần: Nhưng nếu đèn đỏ tớ đây mà bật sáng, thì tất cả ô tô, xe máy, xe đạp…đều phải dừng lại hết. Thế nên tớ mới là người quan trọng nhất chứ!');
insert into [main].[Slide] values(5, 1, 5, 'denvang1', 'Chuyện của đèn vàng', 'Đèn vàng cũng lo mất phần mình nên vội vàng nói: Các cậu không thấy lúc đèn vàng sáng thì các phương tiện giao thông đều chủ động đi chậm lại để chuẩn bị dừng sao? Tớ quan trong hơn các cậu chứ!');
insert into [main].[Slide] values(6, 1, 6, 'oto1', '3 đèn vẫn cứ tranh luận', 'Cuộc tranh luận diễn ra thật sôi nổi, không ai chịu nhường ai. Thật may, đậu trên cột đèn tín hiệu có hai chú chim nhỏ đáng yêu, nãy giờ cũng đang theo dõi phần tranh luận của hai bạn nhỏ');
insert into [main].[Slide] values(7, 1, 7, 'chim1', 'chú chim phân xư', 'Thôi nào, ba bạn đèn xanh, đèn vàng, đèn đỏ hãy nghe mình nói. Cả ba bạn đều quan trọng như nhau mà. Này nhé, bạn đèn vàng thì mọi người phải giảm tốc độ, chuẩn bị dừng, đèn đỏ bật sáng thì tất cả các phương tiện phải dừng lại, và nếu đèn xanh thì mọi người đều đi. Nếu thiếu bất kì ai trong 3 các bạn thì giao thông ở các ngã 3, ngã tư không thể thông suốt được. Vì vậy các bạn hãy đoàn kết và cùng  nhau làm tốt nhiệm vụ của mình nhé.');
insert into [main].[Slide] values(8, 1, 8, 'den1', 'Đoạn kết', 'Ba bạn đèn xanh, đèn đỏ, đèn vàng hiểu ra, bẽn lẽn cười và cảm ơn hai bạn chim. Từ đó họ sống hòa thuận bên nhau, cùng làm tốt việc báo tín hiệu điều khiển giao thông');
insert into [main].[Slide] values(1, 1, 1, 'oto1', 'Xin chào, tớ  là taxi', 'Chào các bạn nhỏ, tôi là Taxi đáng yêu, hôm nay tôi sẽ kể cho các bạn nghe một câu chuyện rất thú vị.');


/* Commit Transaction */
commit transaction;

/* Enable Foreign Keys */
pragma foreign_keys = on;
