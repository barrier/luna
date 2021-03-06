package org.woodship.luna.core.security;

import org.woodship.luna.util.JPAContainerItemFieldGroup;
import org.woodship.luna.util.Utils;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class RoleEditor extends Window  {
	JPAContainer<Role> persons = null;
	JPAContainerItem<Role> jpaitem = null;

	@SuppressWarnings("unchecked")
	public RoleEditor(final Item item,  final JPAContainer<Role> persons) {
		//TODO 角色编辑中限制如果选择本单位或本（顶级）部门必须关联人员
		this.setCaption("角色编辑/新增");
		this.persons = persons;
		this.jpaitem = (JPAContainerItem<Role>) item;
		final FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		final JPAContainerItemFieldGroup<Role> fg = new JPAContainerItemFieldGroup<Role>(Role.class);
		fg.setItemDataSource(jpaitem);
		
		//增加默认字段
		Utils.buildAndBindFieldGroup(fg, Role.class, formLayout);
//		formLayout.addComponent(fg.buildAndBind("trueName"));

		// Buffer the form content
		fg.setBuffered(true);

		//处理保存事件
		Button saveButton = new Button("保存");
		saveButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					//编辑的直接提交即可
					fg.commit();
					//新增的需要单独处理
					if(jpaitem.getEntity().getId() == null){
						Role p =fg.getItemDataSource().getEntity();
						persons.addEntity(p);
					}
					Notification.show("保存成功");
//					error.setVisible(false);
					RoleEditor.this.close();//关闭，防止再点击，重复增加
				} catch (FieldGroup.CommitException e) {
					Utils.setCommitExceptionMsg(e, fg);
				}
			}
		});
		Button cancelButton = new Button("重置");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				fg.discard();
			}
		});
		
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setMargin(true);
		buttons.addComponent(saveButton);
		buttons.addComponent(cancelButton);
		formLayout.addComponent(buttons);
		formLayout.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);
		setContent(formLayout);
	}

}
