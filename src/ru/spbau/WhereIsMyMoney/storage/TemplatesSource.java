package ru.spbau.WhereIsMyMoney.storage;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.WhereIsMyMoney.parser.Template;
import android.content.Context;
import android.database.Cursor;

public class TemplatesSource extends BaseDataSource {
	public TemplatesSource(Context context) {
		super(new TemplatesHelper(context));
	}
	
	public void addTemplate(Template template) {
		TemplatesHelper.addTemplate(template, getDatabase());
	}
	
	public List<Template> getTemplates() {
		List<Template> templates = new ArrayList<Template>();
		Cursor cursor = getDatabase().query(TemplatesHelper.TABLE_TEMPLATES,
				TemplatesHelper.ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			templates.add(cursorToTemplate(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return templates;
	}
	
	private Template cursorToTemplate(Cursor cursor) {
		String template = cursor.getString(cursor.getColumnIndex(TemplatesHelper.COLUMN_TEMPLATE));
		int type = cursor.getInt(cursor.getColumnIndex(TemplatesHelper.COLUMN_TYPE));
		return new Template(template, type);
	}
}
