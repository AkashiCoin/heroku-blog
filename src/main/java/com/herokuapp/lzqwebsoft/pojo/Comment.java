package com.herokuapp.lzqwebsoft.pojo;

import java.util.Date;
import java.util.Set;

/**
 * 封装文章的评论对象
 * @author zqluo
 *
 */
public class Comment {
	private int id;
	private Article article;              // 关联的一篇文章
	private String reviewer;
	private String website;
	private String content;
	private Comment parentComment;        // 对应的父评论
	private Set<Comment> childComments;   // 对应的子评论
	private Date createAt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Comment getParentComment() {
		return parentComment;
	}
	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}
	public Set<Comment> getChildComments() {
		return childComments;
	}
	public void setChildComments(Set<Comment> childComments) {
		this.childComments = childComments;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
