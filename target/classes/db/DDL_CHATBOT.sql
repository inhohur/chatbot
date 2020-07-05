
CREATE TABLE skillsession (
	userid VARCHAR(100) NOT NULL,
	bot VARCHAR(20) NOT NULL,
	skill VARCHAR(50) NOT NULL,
	currentsection VARCHAR(30) NOT NULL,
	sessionvariables VARCHAR(200)
)

ALTER TABLE skillsession ADD UNIQUE(userid, bot);

CREATE TABLE skilldata (
	bot VARCHAR(20) NOT NULL,
	skill VARCHAR(50) NOT NULL,
	section VARCHAR(20) NOT NULL,
	sequence INT NOT NULL,
	type VARCHAR(20) NOT NULL,
	content VARCHAR(200)
);

ALTER TABLE skilldata ADD UNIQUE(bot, skill, section, sequence);

