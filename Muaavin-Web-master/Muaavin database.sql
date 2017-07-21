create database Muaavin;
use  Muaavin;
create table groupTable(id int unsigned auto_increment primary key not null, name varchar(50) not null);
create table infringingUsers(User_ID varchar(100) not null, Name varchar(100) not null,Profile_pic varchar(1000),state varchar(100) not null, PRIMARY KEY (User_ID));
create table Users(id varchar(100) not null,name varchar(100) not null, profilePic varchar(1000) not null, state varchar(100) not null , PRIMARY KEY(id) );
create table postTable(id varchar(100) not null, group_name varchar(50) not null, post_Detail varchar(2000),User_ID varchar(100) not null ,InfringingUserID varchar(100) not null,  Post_Image varchar(1000), PRIMARY KEY(id,group_name,User_ID,InfringingUserID));
create table Comments(User_ID varchar(100) not null, InfringingUserId varchar(100) not null, PostId varchar(100) not null,Parent_Comment_id varchar(100) not null,Comment_ID varchar(100) not null,Group_Name varchar(50) not null, Comment varchar(2000), PRIMARY KEY(PostId,Comment_ID,User_ID,InfringingUserId,Group_Name,Parent_Comment_id));
create table Posts_Comments_Table(Post_ID varchar(100) not null , Comment_ID varchar(100) not null , PRIMARY KEY(Post_ID,Comment_ID));
create table userFeedBack(id int unsigned auto_increment primary key not null, post_id varchar(100) not null, user_id varchar(100) not null, comment varchar(2000));
create table ThumbsDown(post_id varchar(100) not null, user_id varchar(100) not null, PRIMARY KEY(post_id,user_id));
create table CommentsThumbDown(post_id varchar(100) not null, commentId varchar(100) not null, PcommentId varchar(100), user_id varchar(100) not null, InfringingUserId varchar(100) not null, PRIMARY KEY(post_id,user_id,commentId, PcommentId));
create table PostFeedBack(id int unsigned auto_increment primary key not null, post_id varchar(100) not null, user_id varchar(100) not null, comment varchar(2000));
create table CommentFeedBack(id int unsigned auto_increment primary key not null, post_id varchar(100) not null, Pcommentid varchar(100) , Comment_id varchar(100) not null, user_id varchar(100) not null, InfriningUserId varchar(100) not null, comment varchar(2000));

CREATE VIEW ThumbsDown_View AS
select ThumbsDown.post_id, count(*) AS  total_Unlikes
from ThumbsDown
group by ThumbsDown.post_id;

CREATE VIEW  CommentsThumbDown_View as
select post_id, PcommentId, commentId, count(*)  as  total_Unlikes
from CommentsThumbDown
group by post_id, PcommentId, commentId;

Create View GroupAPostDetailWithThumbdown AS
select distinct posttable.id, posttable.Post_Image, posttable.post_Detail, ThumbsDown_View.total_unlikes from PostTable 
left join  (ThumbsDown_View) on posttable.id = ThumbsDown_View.post_id
where posttable.group_name = 'A';

Create View GroupBPostDetailWithThumbdown AS
select distinct posttable.id, posttable.Post_Image, posttable.post_Detail, ThumbsDown_View.total_unlikes from PostTable 
left join  (ThumbsDown_View) on posttable.id = ThumbsDown_View.post_id
where posttable.group_name = 'B';

Create View GroupCPostDetailWithThumbdown AS
select distinct posttable.id, posttable.Post_Image, posttable.post_Detail, ThumbsDown_View.total_unlikes from PostTable 
left join  (ThumbsDown_View) on posttable.id = ThumbsDown_View.post_id
where posttable.group_name = 'C';

Create View GroupAllPostDetailWithThumbdown AS
select distinct posttable.id, posttable.Post_Image, posttable.post_Detail, ThumbsDown_View.total_unlikes from PostTable 
left join  (ThumbsDown_View) on posttable.id = ThumbsDown_View.post_id;

Create View GroupAPostDetailWithFeedBack AS
select  GroupAPostDetailWithThumbdown.*, postfeedback.comment from GroupAPostDetailWithThumbdown 
left join (PostFeedBack) on  GroupAPostDetailWithThumbdown.id = postfeedback.post_id;

Create View GroupBPostDetailWithFeedBack AS
select  GroupBPostDetailWithThumbdown.*, postfeedback.comment from GroupBPostDetailWithThumbdown
left join (PostFeedBack) on  GroupBPostDetailWithThumbdown.id = postfeedback.post_id;

Create View GroupCPostDetailWithFeedBack AS
select  GroupCPostDetailWithThumbdown.*, postfeedback.comment from GroupCPostDetailWithThumbdown
left join (PostFeedBack) on  GroupCPostDetailWithThumbdown.id = postfeedback.post_id;

Create View GroupAllPostDetailWithFeedBack AS
select  GroupAllPostDetailWithThumbdown.*, postfeedback.comment from GroupAllPostDetailWithThumbdown
left join (PostFeedBack) on  GroupAllPostDetailWithThumbdown.id = postfeedback.post_id;

Drop View Group_ACommentDetail;
DROP View Group_BCommentDetail;
Drop View Group_CCommentDetail;
Drop View Group_AllCommentDetail;

CREATE VIEW  Group_ACommentDetail AS
select distinct PostId as id, Parent_Comment_id,  comments.Comment_ID, Comment, InfringingUserId , Name, Profile_pic, Comments.User_ID from  Comments, infringingUsers where Group_Name = 'A' and comments.InfringingUserId =infringingUsers.User_ID;
CREATE VIEW  Group_BCommentDetail AS
select distinct PostId as id, Parent_Comment_id,  comments.Comment_ID, Comment, InfringingUserId , Name, Profile_pic, Comments.User_ID from  Comments, infringingUsers where Group_Name = 'B' and comments.InfringingUserId =infringingUsers.User_ID;
CREATE VIEW  Group_CCommentDetail AS
select distinct PostId as id, Parent_Comment_id,  comments.Comment_ID, Comment, InfringingUserId , Name, Profile_pic, Comments.User_ID from  Comments, infringingUsers where Group_Name = 'C' and comments.InfringingUserId =infringingUsers.User_ID;
CREATE VIEW  Group_AllCommentDetail AS
select distinct PostId as id, Parent_Comment_id,  comments.Comment_ID, Comment, InfringingUserId , Name, Profile_pic, Comments.User_ID from  Comments, infringingUsers where comments.InfringingUserId =infringingUsers.User_ID;

Drop View Group_ACommentDetailWithThumbDown;
DROP View Group_BCommentDetailWithThumbDown;
Drop View Group_CCommentDetailWithThumbDown;
Drop View Group_AllCommentDetailWithThumbDown;

CREATE VIEW Group_ACommentDetailWithThumbDown AS
select Group_ACommentDetail.* , commentsThumbDown_View.total_unlikes
from  Group_ACommentDetail
left join(commentsThumbDown_View)
on Group_ACommentDetail.id = commentsThumbDown_View.post_id
and Group_ACommentDetail.Parent_Comment_id = commentsThumbDown_View.PcommentId
and Group_ACommentDetail.Comment_ID = commentsThumbDown_View.commentId;

CREATE VIEW Group_BCommentDetailWithThumbDown AS
select Group_BCommentDetail.* , commentsThumbDown_View.total_unlikes
from  Group_BCommentDetail
left join(commentsThumbDown_View)
on Group_BCommentDetail.id = commentsThumbDown_View.post_id
and Group_BCommentDetail.Parent_Comment_id = commentsThumbDown_View.PcommentId
and Group_BCommentDetail.Comment_ID = commentsThumbDown_View.commentId;

CREATE VIEW Group_CCommentDetailWithThumbDown AS
select Group_CCommentDetail.* , commentsThumbDown_View.total_unlikes
from  Group_CCommentDetail
left join(commentsThumbDown_View)
on Group_CCommentDetail.id = commentsThumbDown_View.post_id
and Group_CCommentDetail.Parent_Comment_id = commentsThumbDown_View.PcommentId
and Group_CCommentDetail.Comment_ID = commentsThumbDown_View.commentId;

CREATE VIEW Group_AllCommentDetailWithThumbDown AS
select Group_AllCommentDetail.* , commentsThumbDown_View.total_unlikes
from  Group_AllCommentDetail
left join(commentsThumbDown_View)
on Group_AllCommentDetail.id = commentsThumbDown_View.post_id
and Group_AllCommentDetail.Parent_Comment_id = commentsThumbDown_View.PcommentId
and Group_AllCommentDetail.Comment_ID = commentsThumbDown_View.commentId;


Drop View Group_AllCommentDetailWithFeedBack;
DROP View Group_ACommentDetailWithFeedBack;
Drop View Group_BCommentDetailWithFeedBack;
Drop View Group_CCommentDetailWithFeedBack;

CREATE VIEW Group_AllCommentDetailWithFeedBack AS
select  Group_AllCommentDetailWithThumbDown.* , CommentFeedBack.comment as FeedBackMessage
from Group_AllCommentDetailWithThumbDown
left join (CommentFeedBack) on  Group_AllCommentDetailWithThumbDown.id = CommentFeedBack.post_id and  Group_AllCommentDetailWithThumbDown.Comment_id = CommentFeedBack.Comment_id
and  Group_AllCommentDetailWithThumbDown.Parent_Comment_id = CommentFeedBack.Pcommentid;

CREATE VIEW Group_ACommentDetailWithFeedBack AS
select  Group_ACommentDetailWithThumbDown.* , CommentFeedBack.comment as FeedBackMessage
from Group_ACommentDetailWithThumbDown
left join (CommentFeedBack) on  Group_ACommentDetailWithThumbDown.id = CommentFeedBack.post_id and  Group_ACommentDetailWithThumbDown.Comment_id = CommentFeedBack.Comment_id
and  Group_ACommentDetailWithThumbDown.Parent_Comment_id = CommentFeedBack.Pcommentid;

CREATE VIEW Group_BCommentDetailWithFeedBack AS
select  Group_BCommentDetailWithThumbDown.* , CommentFeedBack.comment as FeedBackMessage
from Group_BCommentDetailWithThumbDown
left join (CommentFeedBack) on  Group_BCommentDetailWithThumbDown.id = CommentFeedBack.post_id and  Group_BCommentDetailWithThumbDown.Comment_id = CommentFeedBack.Comment_id
and  Group_BCommentDetailWithThumbDown.Parent_Comment_id = CommentFeedBack.Pcommentid;

CREATE VIEW Group_CCommentDetailWithFeedBack AS
select  Group_CCommentDetailWithThumbDown.* , CommentFeedBack.comment as FeedBackMessage
from Group_CCommentDetailWithThumbDown
left join (CommentFeedBack) on  Group_CCommentDetailWithThumbDown.id = CommentFeedBack.post_id and  Group_CCommentDetailWithThumbDown.Comment_id = CommentFeedBack.Comment_id
and  Group_CCommentDetailWithThumbDown.Parent_Comment_id = CommentFeedBack.Pcommentid;






create table BlockUser(UserID varchar(100) not null PRIMARY KEY);

CREATE VIEW BlockedUsers AS
select User_ID from infringingUsers
where state = 'Blocked'
union
select id from users
where state = 'Blocked';


create table TwitterUsers(id varchar(100) not null,name varchar(100) not null, profilePic varchar(1000) not null, state varchar(100) not null , PRIMARY KEY(id));
create table Twitter_InfringingUsers(id varchar(100) not null,name varchar(100) not null, profilePic varchar(1000) not null, state varchar(100) not null , PRIMARY KEY(id));
create table TweetTable(TweetID varchar(100) not null,message varchar(1000) , ImageUrl varchar(1000) ,User_ID varchar(100) not null,Infringing_User_ID varchar(100) not null,Group_Name varchar(100) not null, PRIMARY KEY(TweetID, Group_Name,User_ID ));

CREATE VIEW  TwitterInfringingUsers AS
select distinct Twitter_InfringingUsers.id , Twitter_InfringingUsers.name , Twitter_InfringingUsers.profilePic, Twitter_InfringingUsers.state , TweetTable.Group_Name , TweetTable.User_ID 
from TweetTable, Twitter_InfringingUsers 
where TweetTable.Infringing_User_ID =Twitter_InfringingUsers.id;

Create View  FacebookInfringingUsers AS
select infringingUsers.User_ID as id, Name, Profile_pic,state , group_name, comments.User_ID from comments, infringingusers
where infringingusers.User_ID = comments.InfringingUserID;

Create view BlockedTwitterUsers AS
select id as User_ID from TwitterUsers where state = 'Blocked'
union
select id as User_ID from Twitter_InfringingUsers where state = 'Blocked';

create table TwitterThumbsDown(TweetID varchar(100) not null, User_ID varchar(100), Primary Key (TweetID,User_ID));
create table TwitterFeedBack(TweetID varchar(100) , User_ID varchar(100), Message varchar (1000) ,id int unsigned auto_increment primary key not null);

CREATE VIEW TwitterThumbsDown_View AS
select TwitterThumbsDown.TweetID, count(*) AS  total_Unlikes
from TwitterThumbsDown
group by TwitterThumbsDown.TweetID;

Drop View GroupA_TweetDetail;
DROP View GroupB_TweetDetail;
Drop View GroupC_TweetDetail;
Drop View GroupAll_TweetDetail;

CREATE VIEW  GroupA_TweetDetail AS
select distinct TweetID, ImageUrl, message, id, name, profilePic, tweetTable.User_ID from TweetTable , twitterInfringingUsers where TweetTable.Group_Name = 'A' and tweetTable.Infringing_User_ID = twitterInfringingUsers.id;
CREATE VIEW  GroupB_TweetDetail AS
select distinct TweetID, ImageUrl, message, id, name, profilePic, tweetTable.User_ID from TweetTable , twitterInfringingUsers where TweetTable.Group_Name = 'B' and tweetTable.Infringing_User_ID = twitterInfringingUsers.id;
CREATE VIEW  GroupC_TweetDetail AS
select distinct TweetID, ImageUrl, message, id, name, profilePic, tweetTable.User_ID from TweetTable , twitterInfringingUsers where TweetTable.Group_Name = 'C' and tweetTable.Infringing_User_ID = twitterInfringingUsers.id;
CREATE VIEW  GroupAll_TweetDetail AS
select distinct TweetID, ImageUrl, message, id, name, profilePic, tweetTable.User_ID from TweetTable , twitterInfringingUsers where tweetTable.Infringing_User_ID = twitterInfringingUsers.id;

Drop View GroupA_TweetDetailWithThumbDown;
DROP View GroupB_TweetDetailWithThumbDown;
Drop View GroupC_TweetDetailWithThumbDown;
Drop View GroupAll_TweetDetailWithThumbDown;

CREATE VIEW GroupA_TweetDetailWithThumbDown AS
select distinct  GroupA_TweetDetail.*, TwitterThumbsDown_View.total_unlikes from  GroupA_TweetDetail left join (TwitterThumbsDown_View) on  GroupA_TweetDetail.TweetID = TwitterThumbsDown_View.TweetID;
CREATE VIEW GroupB_TweetDetailWithThumbDown AS
select distinct  GroupB_TweetDetail.*, TwitterThumbsDown_View.total_unlikes from  GroupB_TweetDetail left join (TwitterThumbsDown_View) on  GroupB_TweetDetail.TweetID = TwitterThumbsDown_View.TweetID;
CREATE VIEW GroupC_TweetDetailWithThumbDown AS
select distinct  GroupC_TweetDetail.*, TwitterThumbsDown_View.total_unlikes from  GroupC_TweetDetail left join (TwitterThumbsDown_View) on  GroupC_TweetDetail.TweetID = TwitterThumbsDown_View.TweetID;
CREATE VIEW GroupAll_TweetDetailWithThumbDown AS
select distinct  GroupAll_TweetDetail.*, TwitterThumbsDown_View.total_unlikes from  GroupAll_TweetDetail left join (TwitterThumbsDown_View) on  GroupAll_TweetDetail.TweetID = TwitterThumbsDown_View.TweetID;

Drop View GroupA_TweetDetailWithFeedBack;
DROP View GroupB_TweetDetailWithFeedBack;
Drop View GroupC_TweetDetailWithFeedBack;
Drop View GroupAll_TweetDetailWithFeedBack;

CREATE VIEW GroupA_TweetDetailWithFeedBack AS
select GroupA_TweetDetailWithThumbDown.* , TwitterFeedBack.message as FeedBackMessage from GroupA_TweetDetailWithThumbDown left join(TwitterFeedBack) on GroupA_TweetDetailWithThumbDown.TweetID = TwitterFeedBack.TweetID;
CREATE VIEW GroupB_TweetDetailWithFeedBack AS
select GroupB_TweetDetailWithThumbDown.* , TwitterFeedBack.message as FeedBackMessage from GroupB_TweetDetailWithThumbDown left join(TwitterFeedBack) on GroupB_TweetDetailWithThumbDown.TweetID = TwitterFeedBack.TweetID;
CREATE VIEW GroupC_TweetDetailWithFeedBack AS
select GroupC_TweetDetailWithThumbDown.* , TwitterFeedBack.message as FeedBackMessage from GroupC_TweetDetailWithThumbDown left join(TwitterFeedBack) on GroupC_TweetDetailWithThumbDown.TweetID = TwitterFeedBack.TweetID;
CREATE VIEW GroupAll_TweetDetailWithFeedBack AS
select GroupAll_TweetDetailWithThumbDown.* , TwitterFeedBack.message as FeedBackMessage from GroupAll_TweetDetailWithThumbDown left join(TwitterFeedBack) on GroupAll_TweetDetailWithThumbDown.TweetID = TwitterFeedBack.TweetID;

