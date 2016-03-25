//package yjjc.cl.com.androidstudy;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.Color;
//import android.graphics.drawable.AnimationDrawable;
//import android.graphics.drawable.Drawable;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.text.ClipboardManager;
//import android.text.Editable;
//import android.text.Selection;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnKeyListener;
//import android.view.View.OnTouchListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bmcc.iwork.IWorkApplication;
//import com.bmcc.iwork.R;
//import com.bmcc.iwork.adapter.ChatAdapter;
//import com.bmcc.iwork.adapter.ChatAdapter.MessageItemLongClickListener;
//import com.bmcc.iwork.adapter.EmotionGridAdapter;
//import com.bmcc.iwork.db.CursorUtils;
//import com.bmcc.iwork.db.MessageDBUtils;
//import com.bmcc.iwork.db.Table_Message;
//import com.bmcc.iwork.module.Emotion;
//import com.bmcc.iwork.module.UserVO;
//import com.bmcc.iwork.net.FileUploadManager;
//import com.bmcc.iwork.net.MessageDownloadManager;
//import com.bmcc.iwork.utils.AppUtils;
//import com.bmcc.iwork.utils.BitmapUtils;
//import com.bmcc.iwork.utils.ContactSelectUtil;
//import com.bmcc.iwork.utils.DensityUtil;
//import com.bmcc.iwork.utils.EmotionUtils;
//import com.bmcc.iwork.utils.FileUtils;
//import com.bmcc.iwork.utils.FileUtils.DirType;
//import com.bmcc.iwork.utils.PortalForegroundColorSpan;
//import com.bmcc.iwork.utils.Utils;
//import com.bmcc.iwork.view.RecordButton;
//import com.bmcc.iwork.view.RecordButton.OnFinishedRecordListener;
//import com.bmcc.iwork.view.refreshlistview.RefreshListView.OnTopRefreshListener;
//import com.bmcc.iwork.xmpp.MessageObserver;
//import com.bmcc.iwork.xmpp.MessageObserverManager;
//import com.bmcc.iwork.xmpp.XmppManager;
//import com.bmcc.iwork.xmpp.model.FileMessage;
//import com.bmcc.iwork.xmpp.model.IEventMessage;
//import com.bmcc.iwork.xmpp.model.IEventMessage.EventType;
//import com.bmcc.iwork.xmpp.model.IMessage;
//import com.bmcc.iwork.xmpp.model.IMessage.ChatType;
//import com.bmcc.iwork.xmpp.model.IMessage.MsgType;
//import com.bmcc.iwork.xmpp.model.IMessage.SendState;
//import com.bmcc.iwork.xmpp.model.ImageMessage;
//import com.bmcc.iwork.xmpp.model.RevokeEventMessage;
//import com.bmcc.iwork.xmpp.model.TextMessage;
//import com.bmcc.iwork.xmpp.model.VoiceMessage;
//import com.bmcc.iwork.xmpp.model.YuQingMessage;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.view.annotation.ContentView;
//import com.lidroid.xutils.view.annotation.ViewInject;
//
//@SuppressWarnings("deprecation")
//@ContentView(R.layout.activity_chat)
//public class ChatActivity extends TitleActivity implements OnClickListener, SensorEventListener, OnItemClickListener, MessageObserver,
//		ChatAdapter.OnMediaListener, MessageItemLongClickListener {
//	/**
//	 * title显示名称，群组聊天时候是群组的名称，个人聊天时候是个人中文名
//	 */
//	private String showName = "";
//	private String taJid = ""; // TaJid
//	private String myJid = ""; // MyJid
//	private ChatAdapter adapter;
//	private ArrayList<Emotion> emotions = EmotionUtils.getEmotions();
//	private List<IMessage> msgList = null;
//
//	@ViewInject(R.id.record_button)
//	private RecordButton mRecordButton;
//
//	@ViewInject(R.id.send_camera_bt)
//	private TextView sendCameraBT;
//
//	@ViewInject(R.id.send_image_bt)
//	private TextView sendImageBT;
//
//	@ViewInject(R.id.send_file_bt)
//	private TextView sendFileBT;
//
//	@ViewInject(R.id.send_emotion_bt)
//	private TextView send_emotion_bt;
//
//	@ViewInject(R.id.send_immediately_msg)
//	private TextView sendImmdMsg;
//
//	@ViewInject(R.id.btnAddMessage)
//	private ImageView btnAddMessage;
//
//	@ViewInject(R.id.btnVoice)
//	private ImageView btnVoice;
//
//	@ViewInject(R.id.methodLayout)
//	private View methodLayout;
//
//	@ViewInject(R.id.tvMsgText)
//	private EditText tvMsgText;
//
//	@ViewInject(R.id.chatListview)
//	private com.bmcc.iwork.view.refreshlistview.RefreshListView chatListView;
//
//	@ViewInject(R.id.emotionGrid)
//	private GridView emotionGrid;
//
//	@ViewInject(R.id.ivEmotion)
//	private ImageView ivEmotion;
//
//	@ViewInject(R.id.btnSend)
//	private Button btnSend;
//	private int chatType;
//	private String groupName;
//	private String extraPath = null;
//
//	private AudioManager audioManager = null; // 声音管理器
//	private SensorManager _sensorManager = null; // 传感器管理器
//	private Sensor mProximiny = null; // 传感器实例
//	private float f_proximiny; // 当前传感器距离
//	private int currentMsgPage = 0;
//	private final int request_camera = 1, request_photo = 2, request_file = 3, request_atuser = 4;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		init();
//		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//
//		_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		mProximiny = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//		mMediaPlayer = new MediaPlayer();
//
//		// new MessageLoadTask(taJid, groupName).execute();
//		chatListView.post(new Runnable() {
//
//			@Override
//			public void run() {
//				chatListView.startUpdateImmediate();
//			}
//		});
//		MessageObserverManager.getInstance().registeObserver(this);
//		MessageDownloadManager.getInstance().addDownloadObserver(downloadObserver);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		_sensorManager.registerListener(this, mProximiny, SensorManager.SENSOR_DELAY_NORMAL);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (methodLayout.isShown()) {
//				methodLayout.setVisibility(View.GONE);
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		_sensorManager.unregisterListener(this);
//	}
//
//	private void changeOutputMode(boolean b) {
//		if (b) {
//			// audioManager.setSpeakerphoneOn(b);
//			// // Toast.makeText(ChatActivity.this, "已切换为扬声器模式", 1).show();
//			// audioManager.setMode(AudioManager.MODE_NORMAL);//三星使用这个方法就可以了.小米的都用上也不行.
//			try {
//				audioManager.setSpeakerphoneOn(true);
//				audioManager.setMode(AudioManager.MODE_NORMAL);
//				if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//					Toast.makeText(ChatActivity.this, "已切换为到扬声器模式", Toast.LENGTH_SHORT).show();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			// audioManager.setSpeakerphoneOn(b);
//			// // audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
//			// // setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//			// //把声音设定成Earpiece（听筒）出来，设定为正在通话中
//			// audioManager.setMode(AudioManager.MODE_IN_CALL);
//			try {
//				if (audioManager != null) {
//					audioManager.setSpeakerphoneOn(false);
//					audioManager.setMode(AudioManager.MODE_IN_CALL);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private void init() {
//		this.chatType = getIntent().getIntExtra("chatType", 0);
//		this.myJid = getIntent().getStringExtra("MyJid");
//		if (myJid != null && myJid.contains("/")) {
//			myJid = myJid.substring(0, myJid.indexOf("/") + 1).replace("/", "");
//		}
//		this.taJid = getIntent().getStringExtra("TaJid");
//		this.groupName = getIntent().getStringExtra("GroupName");
//		if (groupName != null && chatType == ChatType.groupchat.intValue()) {
//			taJid = groupName;
//		}
//		this.showName = getIntent().getStringExtra("ShowName");
//		this.extraPath = getIntent().getStringExtra("extraPath");// 从其他应用分享图片等的文件路径
//		if (chatType == ChatType.groupchat.intValue()) {
//			tvMsgText.addTextChangedListener(new TextWatcher() {
//
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before, int count) {
//					Log.i("iwork", s.toString());
//				}
//
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//					Log.i("iwork", s.toString());
//				}
//
//				@Override
//				public void afterTextChanged(Editable s) {
//					String content = s.toString();
//					int len = content.length();
//					if (len > 0) {
//						boolean isAT = content.endsWith("@");
//						if (isAT) {
//							startAt();
//						}
//					}
//				}
//			});
//			tvMsgText.setOnKeyListener(new OnKeyListener() {
//
//				@Override
//				public boolean onKey(View v, int keyCode, KeyEvent event) {
//					if (keyCode == KeyEvent.KEYCODE_DEL) {
//						Editable s = tvMsgText.getText();
//						PortalForegroundColorSpan[] spans = s.getSpans(0, s.length(), PortalForegroundColorSpan.class);
//						for (PortalForegroundColorSpan span : spans) {
//							int start = s.getSpanStart(span);
//							int end = s.getSpanEnd(span);
//							if (!s.toString().substring(start, end).equals(span.keyWords())) {
//								// s.removeSpan(span);
//								s.delete(start, end);
//							}
//						}
//					}
//					Log.i("iwork", "" + keyCode);
//					return false;
//				}
//			});
//		}
//
//		ivRight = (ImageView) getRightMenu();
//		ivRight.setVisibility(View.VISIBLE);
//		ivRight.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (chatType == ChatType.groupchat.intValue()) {
//					Intent intent = new Intent(getApplicationContext(), GroupInfoActivity.class);
//					String groupId = groupName;
//					if (groupName.indexOf("@") != -1) {
//						groupId = groupName.substring(0, groupName.indexOf("@"));
//					}
//					intent.putExtra("taJid", groupName + "");
//					intent.putExtra("groupName", groupId + "");
//					intent.putExtra("displayName", showName + "");
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(getApplicationContext(), ChatSettingActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//					intent.putExtra("taJid", taJid);
//					startActivity(intent);
//				}
//			}
//		});
//
//		setTitle(showName);
//		if (chatType == ChatType.groupchat.intValue()) {
//			ivRight.setImageResource(R.drawable.ic_mygroup);
//			MessageDBUtils.getInstance().setThreadReaded(chatType, "groupname", new String[] { groupName });
//		} else {
//			ivRight.setImageResource(R.drawable.icon_myinfo);
//			MessageDBUtils.getInstance().setThreadReaded(chatType, "tos", new String[] { taJid });
//		}
//
//		emotionGrid.setAdapter(new EmotionGridAdapter(this, emotions));
//		emotionGrid.setOnItemClickListener(this);
//		// 获取文本信息
//		tvMsgText.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				hideToolView();
//				return false;
//			}
//		});
//		tvMsgText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if (s.length() > 0) {
//					btnSend.setVisibility(View.VISIBLE);
//					btnAddMessage.setVisibility(View.INVISIBLE);
//				} else {
//					btnSend.setVisibility(View.INVISIBLE);
//					btnAddMessage.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//
//		send_emotion_bt.setOnClickListener(this);
//		sendImmdMsg.setOnClickListener(this);
//		sendCameraBT.setOnClickListener(this);
//		sendImageBT.setOnClickListener(this);
//		sendFileBT.setOnClickListener(this);
//		btnSend.setOnClickListener(this);
//		btnAddMessage.setOnClickListener(this);
//		btnVoice.setOnClickListener(this);
//		ivEmotion.setOnClickListener(this);
//		tvMsgText.setOnClickListener(this);
//
//		int ww = DensityUtil.dp2px(this, 60);
//		Drawable drawable = getResources().getDrawable(R.drawable.chat_camera_icon);
//		drawable.setBounds(0, 0, ww, ww);
//		sendCameraBT.setCompoundDrawables(null, drawable, null, null);
//
//		drawable = getResources().getDrawable(R.drawable.chat_emotion_icon);
//		drawable.setBounds(0, 0, ww, ww);
//		send_emotion_bt.setCompoundDrawables(null, drawable, null, null);
//
//		drawable = getResources().getDrawable(R.drawable.chat_file_icon);
//		drawable.setBounds(0, 0, ww, ww);
//		sendFileBT.setCompoundDrawables(null, drawable, null, null);
//
//		drawable = getResources().getDrawable(R.drawable.chat_picture_icon);
//		drawable.setBounds(0, 0, ww, ww);
//		sendImageBT.setCompoundDrawables(null, drawable, null, null);
//
//		drawable = null;
//
//		chatListView.setTranscriptMode(1);
//		chatListView.setBottomHasMore(false);
//		chatListView.setOnTopRefreshListener(onTopRefreshListener);
//		chatListView.setOnItemClickListener(this);
//		// chatListView.setOnItemLongClickListener(this);
//		// chatListView.setOnTouchListener(new OnTouchListener() {
//		// @Override
//		// public boolean onTouch(View v, MotionEvent event) {
//		// hideKeyboard();
//		// hideToolView();
//		// return false;
//		// }
//		// });
//		msgList = new ArrayList<IMessage>();
//		this.adapter = new ChatAdapter(this, msgList);
//		adapter.setOnMessageLongClickListener(this);
//		chatListView.setAdapter(adapter);
//		adapter.setOnMediaListener(this);
//
//		String path = FileUtils.getPrivateDir(getApplicationContext()).toString();
//		mRecordButton.setSavePath(path);
//		mRecordButton.setOnFinishedRecordListener(new OnFinishedRecordListener() {
//			@Override
//			public void onFinishedRecord(String audioPath, int time) {
//				Log.i("RECORD!!!", "finished!!!!!!!!!! save to " + audioPath);
//				if (audioPath != null) {
//					try {
//						VoiceMessage message = new VoiceMessage(chatType, myJid, taJid);
//						message.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//						message.setTo_zh(showName);
//						message.setGroupname(groupName);
//						message.setFilepath(audioPath);
//						message.setVoicetime(time);
//						msgList.add(message);
//						adapter.notifyDataSetChanged();
//						// adapter.addData(message);
//						MessageDBUtils.getInstance().addMessage(message);
//						// 发送消息
//						sendFile(message, audioPath);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				} else {
//					Toast.makeText(ChatActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});
//
//	}
//
//	/**
//	 * 处理转发消息
//	 */
//	private void processMessage() {
//		IMessage message = ContactSelectUtil.getInstance().getMessage();
//		if (message != null) {
//			message.setMsgid(UUID.randomUUID().toString());
//			message.setFrom(myJid);
//			message.setTo(taJid);
//			message.setType(chatType);
//			message.setSend(1);
//			message.setTo_zh(showName);
//			message.setGroupname(groupName);
//			message.setCreatetime(System.currentTimeMillis());
//			message.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//			ContactSelectUtil.getInstance().setMessage(null);
//
//			msgList.add(message);
//			adapter.notifyDataSetChanged();
//			// adapter.addData(chatMsg);
//			MessageDBUtils.getInstance().addMessage(message);
//			try {
//				// 发送消息
//				XmppManager.getInstance().sendMessagePacket(message);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (!TextUtils.isEmpty(extraPath)) {
//			String mimeType = FileUtils.getMimeType(extraPath);
//			if (mimeType != null && mimeType.startsWith("image")) {
//				ImageMessage myChatMsg = new ImageMessage(chatType, myJid, taJid);
//				myChatMsg.setState(SendState.WAITING.intValue());
//				myChatMsg.setGroupname(groupName);
//				myChatMsg.setFilepath(extraPath);
//				myChatMsg.setSend(1);
//				myChatMsg.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//				myChatMsg.setTo_zh(showName);
//				myChatMsg.setState(SendState.WAITING.intValue());
//				msgList.add(myChatMsg);
//				adapter.notifyDataSetChanged();
//				MessageDBUtils.getInstance().addMessage(myChatMsg);
//				sendFile(myChatMsg, extraPath);
//			}
//		}
//	}
//
//	/**
//	 * at人时候打开选人界面
//	 */
//	private void startAt() {
//		Intent intent = new Intent(getApplicationContext(), GroupMemberActivity.class);
//		intent.putExtra("groupName", groupName);
//		startActivityForResult(intent, request_atuser);
//	}
//
//	private void sendFile(final IMessage message, String filePath) {
//		FileUploadManager.upload(filePath, new RequestCallBack<String>() {
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				try {
//					JSONObject obj = new JSONObject(responseInfo.result);
//					String req_code = obj.optString("req_code");
//					String fileUrl = obj.optString("fileURL");
//					if ("0".equals(req_code)) {
//						message.setState(SendState.FAILURE.intValue());
//						try {
//							long rowId = MessageDBUtils.getInstance().updateMessage(message);
//							Log.w("iwork", "sendFile==============" + rowId);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						AppUtils.notifyMessage(IWorkApplication.getInstance(), message);
//						// Utils.showToast(getApplicationContext(), resp.getRespMsg());
//						return;
//					}
//					String msgtype = message.getMsgtype();
//					Log.e("iwork", msgtype);
//					if (MsgType.file.toString().equals(msgtype)) {
//						FileMessage fileMessage = (FileMessage) message;
//						fileMessage.setFileurl(fileUrl);
//					} else if (MsgType.image.toString().equals(msgtype)) {
//						ImageMessage imageMessage = (ImageMessage) message;
//						imageMessage.setImageurl(fileUrl);
//					} else if (MsgType.voice.toString().equals(msgtype)) {
//						VoiceMessage voiceMessage = (VoiceMessage) message;
//						voiceMessage.setVoiceurl(fileUrl);
//					}
//					try {
//						long rowId = MessageDBUtils.getInstance().updateMessage(message);
//						Log.w("iwork", "sendFile==============" + rowId);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					XmppManager.getInstance().sendMessagePacket(message);
//				} catch (Exception e) {
//				}
//			}
//
//			@Override
//			public void onStart() {
//				super.onStart();
//			}
//
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//				super.onLoading(total, current, isUploading);
//				showDownLoadprogress(message, total, current);
//			}
//
//			@Override
//			public void onFailure(HttpException error, String msg) {
//				message.setState(SendState.FAILURE.intValue());
//				try {
//					MessageDBUtils.getInstance().update(Table_Message.tableName, message.toContentValues(), " msgid=?",
//							new String[] { message.getMsgid() });
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	private void showDownLoadprogress(IMessage msg, long allSize, long downSize) {
//		try {
//			View view = chatListView.findViewWithTag(msg.getMsgid());
//			if (view == null) {
//				return;
//			}
//			if (MsgType.image.toString().equals(msg.getMsgtype()) && allSize > 0) {
//				long pro = downSize * 100 / allSize;
//				if (pro > 100) {
//					pro = 100;
//				}
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("msg", msg);
//				bundle.putLong("progress", pro);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	boolean isVoice = false;
//
//	@Override
//	public void onClick(View v) {
//		if (v == btnVoice) {
//			if (!isVoice) {
//				isVoice = true;
//				mRecordButton.setVisibility(View.VISIBLE);
//				tvMsgText.setVisibility(View.GONE);
//				ivEmotion.setVisibility(View.INVISIBLE);
//				btnVoice.setImageResource(R.drawable.bg_chat_keyboard);
//				btnSend.setVisibility(View.GONE);
//				btnAddMessage.setVisibility(View.VISIBLE);
//				hideKeyboard();
//				hideToolView();
//			} else {
//				isVoice = false;
//				mRecordButton.setVisibility(View.INVISIBLE);
//				tvMsgText.setVisibility(View.VISIBLE);
//				ivEmotion.setVisibility(View.VISIBLE);
//				btnSend.setVisibility(View.VISIBLE);
//				if (tvMsgText.getText().toString().length() > 0) {
//					btnSend.setVisibility(View.VISIBLE);
//					btnAddMessage.setVisibility(View.INVISIBLE);
//				} else {
//					btnSend.setVisibility(View.GONE);
//					btnAddMessage.setVisibility(View.VISIBLE);
//				}
//				btnVoice.setImageResource(R.drawable.bg_chat_voice);
//				showKeyboard();
//				hideToolView();
//			}
//		} else if (v == btnAddMessage) {
//			methodLayout.setVisibility(methodLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//			emotionGrid.setVisibility(View.GONE);
//			hideKeyboard();
//		} else if (v == ivEmotion) {
//			methodLayout.setVisibility(View.GONE);
//			emotionGrid.setVisibility(View.VISIBLE);
//			hideKeyboard();
//		} else if (v == send_emotion_bt) {
//			methodLayout.setVisibility(View.GONE);
//			emotionGrid.setVisibility(View.VISIBLE);
//		} else if (v == tvMsgText) {
//			methodLayout.setVisibility(View.GONE);
//			emotionGrid.setVisibility(View.GONE);
//			showKeyboard();
//		} else if (v == sendFileBT) {
//			Intent intent = new Intent(ChatActivity.this, SearchFilesActivity.class);
//			startActivityForResult(intent, request_file);
//		} else if (v == sendImageBT) {
//			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//				getAlbum.setAction(Intent.ACTION_OPEN_DOCUMENT);
//			} else {
//				getAlbum.setAction(Intent.ACTION_GET_CONTENT);
//			}
//			getAlbum.setType("image/*");
//			startActivityForResult(getAlbum, request_photo);
//		} else if (v == sendCameraBT) {
//			Intent captrueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			startActivityForResult(captrueIntent, request_camera);
//		} else if (v == btnSend) {
//			emotionGrid.setVisibility(View.GONE);
//			// 获取text文本
//			final String msgContent = " " + tvMsgText.getText().toString();
//			if (msgContent.trim().length() > 0) {
//				// 自己显示消息
//				TextMessage chatMsg = (TextMessage) IMessage.createMessage(MsgType.text, chatType, myJid, taJid);
//				chatMsg.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//				chatMsg.setTo_zh(showName);
//				chatMsg.setGroupname(groupName);
//				chatMsg.setContent(msgContent);
//				msgList.add(chatMsg);
//				adapter.notifyDataSetChanged();
//				// adapter.addData(chatMsg);
//				MessageDBUtils.getInstance().addMessage(chatMsg);
//				try {
//					// 发送消息
//					XmppManager.getInstance().sendMessagePacket(chatMsg);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				Toast.makeText(ChatActivity.this, "发送信息不能为空", Toast.LENGTH_SHORT).show();
//			}
//			// 清空text
//			tvMsgText.setText("");
//		}
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode != RESULT_OK || data == null) {
//			return;
//		}
//		switch (requestCode) {
//		case request_camera:
//			handleCameraResult(data);
//			break;
//		case request_photo:
//			handlePhotoResult(data);
//			break;
//		case request_file:
//			// 附件获取路径
//			final String filepath = data.getStringExtra("filepath");
//			if (filepath.length() > 0) {
//				File file = new File(filepath);
//				LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_confirmfile, null);
//				TextView tv = (TextView) view.findViewById(R.id.text);
//				ImageView image = (ImageView) view.findViewById(R.id.image);
//				tv.setText(file.getName());
//				image.setImageDrawable(getResources().getDrawable(R.drawable.ic_filetype));
//
//				new AlertDialog.Builder(this).setTitle("确认发送").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						FileMessage fileMessage = new FileMessage(chatType, myJid, taJid);
//						fileMessage.setGroupname(groupName);
//						fileMessage.setState(SendState.WAITING.intValue());
//						fileMessage.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//						fileMessage.setTo_zh(showName);
//						fileMessage.setFilepath(filepath);
//						msgList.add(fileMessage);
//						adapter.notifyDataSetChanged();
//						MessageDBUtils.getInstance().addMessage(fileMessage);
//						sendFile(fileMessage, filepath);
//
//					}
//				}).setNegativeButton("取消", null).show();
//
//			}
//
//			break;
//		case request_atuser:
//			String srcContent = tvMsgText.getText().toString();
//			if (srcContent.length() > 0) {
//				srcContent = srcContent.substring(0, srcContent.length() - 1);
//				tvMsgText.setText(srcContent);
//			}
//
//			UserVO user = (UserVO) data.getSerializableExtra("user");
//			String name = user.getUserName();
//			name = "@" + name + " ";
//			int len = name.length();
//			SpannableString ss = new SpannableString(name);
//			ss.setSpan(new PortalForegroundColorSpan(Color.BLUE, name), 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			tvMsgText.append(ss);
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void handleCameraResult(Intent data) {
//		Uri originalUri = data.getData(); // 获得图片的uri
//		Bitmap bmp = null;
//		LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_confirmfile, null);
//		TextView tv = (TextView) view.findViewById(R.id.text);
//		ImageView image = (ImageView) view.findViewById(R.id.image);
//		String tempPath = null;
//		File dir = new File(FileUtils.getPrivateDir(getApplicationContext()), DirType.IMAGE);
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//		if (originalUri == null) {
//			Object obj = data.getExtras().get("data");
//			if (obj instanceof Bitmap) {
//				bmp = (Bitmap) obj;
//				File file = new File(dir, System.currentTimeMillis() + ".jpg");
//
//				FileOutputStream fos = null;
//				try {
//					if (!file.exists()) {
//						file.createNewFile();
//					}
//					fos = new FileOutputStream(file);
//					bmp.compress(CompressFormat.JPEG, 100, fos);
//					tempPath = file.getAbsolutePath();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (fos != null) {
//						try {
//							fos.close();
//						} catch (IOException e) {
//							fos = null;
//						}
//					}
//					BitmapUtils.recycleBitmap(bmp);
//				}
//			}
//		} else {
//			String[] proj = { MediaStore.Images.Media.DATA };
//			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			tempPath = cursor.getString(column_index);
//			if (tempPath == null || "".equals(tempPath)) {
//				return;
//			}
//		}
//		Bitmap bitmap = BitmapUtils.getSmallBitmap(tempPath);
//		final String filePath = tempPath;
//		File file = new File(tempPath);
//		tv.setText(file.getName());
//		if (bitmap != null) {
//			image.setImageBitmap(bitmap);
//		} else {
//			image.setImageResource(R.drawable.ic_launcher);
//		}
//
//		new AlertDialog.Builder(this).setTitle("确认发送").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				ImageMessage myChatMsg = new ImageMessage(chatType, myJid, taJid);
//				myChatMsg.setState(SendState.WAITING.intValue());
//				myChatMsg.setGroupname(groupName);
//				myChatMsg.setFilepath(filePath);
//				myChatMsg.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//				myChatMsg.setTo_zh(showName);
//				msgList.add(myChatMsg);
//				adapter.notifyDataSetChanged();
//				MessageDBUtils.getInstance().addMessage(myChatMsg);
//				sendFile(myChatMsg, filePath);
//			}
//		}).setNegativeButton("取消", null).show();
//
//	}
//
//	@SuppressLint("NewApi")
//	private void handlePhotoResult(Intent data) {
//		try {
//			String tempPath = null;
//			// 获取到图片路径
//			Uri contentUri = data.getData(); // 获得图片的uri
//			Cursor cursor = null;
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//				String wholeID = DocumentsContract.getDocumentId(contentUri);
//				String id = wholeID.split(":")[1];
//				String[] column = { MediaStore.Images.Media.DATA };
//				String sel = MediaStore.Images.Media._ID + "=?";
//				cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[] { id }, null);
//				int columnIndex = cursor.getColumnIndex(column[0]);
//				if (cursor.moveToFirst()) {
//					tempPath = cursor.getString(columnIndex);
//				}
//			} else {
//				String[] projection = { MediaStore.Images.Media.DATA };
//				cursor = getContentResolver().query(contentUri, projection, null, null, null);
//				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//				cursor.moveToFirst();
//				tempPath = cursor.getString(column_index);
//			}
//			CursorUtils.closeQuietly(cursor);
//
//			// Cursor cursor = managedQuery(originalUri, null, null, null, null);
//			// String[] names = cursor.getColumnNames();
//			// if (cursor.moveToFirst()) {
//			// for (String name : names) {
//			// Log.e("iwork", " " + cursor.getString(cursor.getColumnIndex(name)));
//			// }
//			//
//			// }
//			// int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			// cursor.moveToFirst();
//			// String tempPath = cursor.getString(column_index);
//			if (tempPath == null || tempPath.trim().length() == 0) {
//				return;
//			}
//			try {
//				tempPath = BitmapUtils.compressImage(ChatActivity.this, tempPath);
//			} catch (Exception e) {
//				Toast.makeText(getApplicationContext(), e.getMessage() + " ", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			final String filepath = tempPath;
//			if (filepath.length() > 0) {
//				File file = new File(filepath);
//				LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_confirmfile, null);
//				TextView tv = (TextView) view.findViewById(R.id.text);
//				ImageView image = (ImageView) view.findViewById(R.id.image);
//				tv.setText(file.getName());
//
//				final Bitmap bmp = BitmapUtils.getSmallBitmap(tempPath);
//				if (bmp != null) {
//					image.setImageBitmap(bmp);
//				} else {
//					image.setImageResource(R.drawable.ic_launcher);
//				}
//
//				new AlertDialog.Builder(this).setTitle("确认发送").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						ImageMessage myChatMsg = new ImageMessage(chatType, myJid, taJid);
//						myChatMsg.setGroupname(groupName);
//						myChatMsg.setState(SendState.WAITING.intValue());
//						myChatMsg.setFrom_zh(IWorkApplication.getInstance().getUser().getUserName());
//						myChatMsg.setFilepath(filepath);
//						myChatMsg.setTo_zh(showName);
//						msgList.add(myChatMsg);
//						adapter.notifyDataSetChanged();
//						MessageDBUtils.getInstance().addMessage(myChatMsg);
//						sendFile(myChatMsg, filepath);
//					}
//				}).setNegativeButton("取消", null).show();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	//显示软件盘
//	private void showKeyboard() {
//		tvMsgText.setFocusableInTouchMode(true); // 设置模式,以便获取焦点
//		tvMsgText.requestFocus();
//		InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
//		im.showSoftInput(tvMsgText, 1);
//
//		chatListView.setSelection(msgList.size() - 1);
//	}
//
//	private void hideKeyboard() {
//		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tvMsgText.getWindowToken(), 0);
//	}
//
//	private void hideToolView() {
//		if (methodLayout.isShown()) {
//			methodLayout.setVisibility(View.GONE);
//		}
//	}
//
//	public void showONLongclickDialog(final IMessage imessage) {
//		// String[] temp1 = {"重新发送"};
//		ArrayList<String> tempArr = new ArrayList<String>();
//		if (MsgType.text.value().equals(imessage.getMsgtype())) {
//			tempArr.add("复制消息");
//			tempArr.add("撤销此消息");
//		}
//		if (IMessage.SendState.FAILURE.intValue() == imessage.getState()) {
//			tempArr.add("重新发送");
//		}
//		final String[] items = tempArr.toArray(new String[tempArr.size()]);
//		if (items.length > 0) {
//			new AlertDialog.Builder(ChatActivity.this).setTitle("选择操作").setItems(items, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int item) {
//					doDialogItem(items[item], imessage);
//				}
//			}).show();
//		}
//	}
//
//	public void doDialogItem(String itemStr, IMessage imessage) {
//		if ("重新发送".equals(itemStr)) {
//			Utils.showToast(getApplicationContext(), "此功能暂未开放");
//		} else if ("复制消息".equals(itemStr)) {
//			ClipboardManager clipboard = (ClipboardManager) ChatActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
//
//			clipboard.setText(((TextMessage) imessage).getContent());
//			Utils.showToast(getApplicationContext(), "已复制到剪切板");
//		} else if ("撤销此消息".equals(itemStr)) {
//			RevokeEventMessage msg = (RevokeEventMessage) IEventMessage.createEventMessage(EventType.revoke, myJid, taJid);
//			msg.setRevoke_packetid(imessage.getPacketid());
//			XmppManager.getInstance().sendMessagePacket(imessage);
//		}
//	}
//
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		f_proximiny = event.values[0];
//
//		if (f_proximiny >= mProximiny.getMaximumRange()) {
//			changeOutputMode(true);
//		} else {
//			changeOutputMode(false);
//		}
//	}
//
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//	}
//
//	private MediaPlayer mMediaPlayer;
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		hideKeyboard();
//		MessageObserverManager.getInstance().unRegisteObserver(this);
//		MessageDownloadManager.getInstance().close();
//		MessageDownloadManager.getInstance().addDownloadObserver(downloadObserver);
//		if (mMediaPlayer == null) {
//			mMediaPlayer = new MediaPlayer();
//		}
//		if (mMediaPlayer.isPlaying()) {
//			mMediaPlayer.stop();
//			mMediaPlayer.reset();
//		}
//		mMediaPlayer = null;
//	}
//
//	private void stopAudio(VoiceMessage message) {
//		if (mMediaPlayer.isPlaying()) {
//			mMediaPlayer.stop();
//			mMediaPlayer.reset();
//		}
//		if (chatListView != null) {
//			ImageView ivVoiceAnim = (ImageView) chatListView.findViewWithTag("voice_" + message.getMsgid());// msg.getMsgid()+"voice"
//			final AnimationDrawable drawable = (AnimationDrawable) ivVoiceAnim.getDrawable();
//			if (ivVoiceAnim != null && drawable != null) {
//				drawable.stop();
//				ivVoiceAnim.clearAnimation();
//				drawable.selectDrawable(0);
//			}
//		}
//	}
//
//	private void playAudio(final VoiceMessage message) {
//
//		try {
//			String filePath = message.getFilepath();
//			File audioFile = new File(filePath);
//			if (!TextUtils.isEmpty(filePath) && audioFile.exists()) {
//				if (mMediaPlayer == null) {
//					mMediaPlayer = new MediaPlayer();
//				}
//				if (mMediaPlayer.isPlaying()) {
//					mMediaPlayer.stop();
//					mMediaPlayer.reset();
//				}
//
//				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//				final ImageView ivVoiceAnim = (ImageView) chatListView.findViewWithTag("voice_" + message.getMsgid());
//
//				if (ivVoiceAnim != null) {
//					final AnimationDrawable drawable = (AnimationDrawable) ivVoiceAnim.getDrawable();
//					mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//						@Override
//						public void onCompletion(MediaPlayer mp) {
//							if (ivVoiceAnim != null && drawable != null) {
//								drawable.stop();
//								ivVoiceAnim.clearAnimation();
//								drawable.selectDrawable(0);
//							}
//						}
//					});
//					FileInputStream fis = null;
//					try {
//						fis = new FileInputStream(audioFile);
//						mMediaPlayer.reset();
//						mMediaPlayer.setDataSource(fis.getFD());
//						mMediaPlayer.prepare();
//					} catch (Exception e) {
//						e.printStackTrace();
//						drawable.stop();
//						drawable.selectDrawable(0);
//					} finally {
//						if (fis != null) {
//							fis.close();
//							fis = null;
//						}
//					}
//					mMediaPlayer.start();
//
//					drawable.start();
//				} else {
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		if (parent instanceof GridView) {
//			Emotion emotion = emotions.get(position);
//			int selectionStart = tvMsgText.getSelectionStart();
//			String emotionStr = emotion.getPhrase();
//			int selection = tvMsgText.getSelectionStart();
//			Editable editable = tvMsgText.getText();
//			editable.insert(selection, emotionStr);
//			tvMsgText.setText(EmotionUtils.parseEmotionStr(this, tvMsgText, tvMsgText.getText().toString()));
//			Editable transpondContentEditable = tvMsgText.getText();
//			Selection.setSelection(transpondContentEditable, selectionStart + emotionStr.length());
//		} else {
//
//		}
//	}
//
//	@Override
//	public void processFileMessage(FileMessage message) {
//		String filePath = message.getFilepath();
//		if (TextUtils.isEmpty(filePath)) {
//			return;
//		}
//		File file = new File(filePath);
//		if (file == null || !file.exists()) {
//			Utils.showToast(getApplicationContext(), "文件不存在");
//		}
//		FileUtils.openFile(getApplicationContext(), filePath);
//	}
//
//	@Override
//	public void processVoiceMessage(VoiceMessage message) {
//		try {
//			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//				stopAudio(message);
//			} else {
//				playAudio(message);
//			}
//		} catch (Exception e) {
//			Utils.showToast(getApplicationContext(), "暂时无法播放");
//		}
//
//	}
//
//	@Override
//	public void processImageMessage(ImageMessage message) {
//		String path = message.getFilepath();
//		if (TextUtils.isEmpty(path)) {
//			return;
//		}
//		Intent intent = new Intent(getApplicationContext(), PreviewPictureActivity.class);
//		intent.putExtra("path", path);
//		startActivity(intent);
//	}
//
//	@Override
//	public void processCardMessage(YuQingMessage message) {
//		Intent intent = new Intent(this, WebBrowerActivity.class);
//		intent.putExtra("message", message);
//		startActivity(intent);
//	}
//
//	@Override
//	public void onMessage(IMessage message) {
//		if (message == null) {
//			currentMsgPage = 0;
//			new MessageLoadTask(taJid, groupName).execute();
//			return;
//		}
//		if (chatType != message.getType()) {
//			return;
//		}
//		if (chatType == ChatType.groupchat.intValue()) {
//			if (groupName == null || message.getGroupname() == null || !message.getGroupname().contains(groupName)) {
//				return;
//			}
//		} else {
//			if (taJid == null || !message.getTo().contains(taJid)) {
//				return;
//			}
//		}
//
//		int index = msgList.indexOf(message);
//		if (index == -1) {
//			msgList.add(message);
//			if (chatType == ChatType.groupchat.intValue()) {
//				MessageDBUtils.getInstance().setThreadReaded(chatType, "groupname", new String[] { groupName });
//			} else {
//				MessageDBUtils.getInstance().setThreadReaded(chatType, "tos", new String[] { taJid });
//			}
//		} else {
//			msgList.set(index, message);
//		}
//		adapter.notifyDataSetChanged();
//	}
//
//	private OnTopRefreshListener onTopRefreshListener = new OnTopRefreshListener() {
//
//		private int msglistSizeBeforeRefresh;
//
//		@Override
//		public void onStart() {
//		}
//
//		@Override
//		public void onDoinBackground() {
//			List<IMessage> result = selectMessageWithNum(currentMsgPage);
//			msglistSizeBeforeRefresh = msgList.size();
//			msgList.addAll(0, result);
//			currentMsgPage++;
//		}
//
//		@Override
//		public void onEnd() {
//			adapter.notifyDataSetChanged();
//			chatListView.setTranscriptMode(1);
//			chatListView.setSelection(msgList.size() - msglistSizeBeforeRefresh);
//			processMessage();
//		}
//
//	};
//
//	private class MessageLoadTask extends AsyncTask<Void, Void, List<IMessage>> {
//		private String taJid, groupName;
//
//		public MessageLoadTask(String taJid, String groupName) {
//			this.taJid = taJid;
//			this.groupName = groupName;
//		}
//
//		@Override
//		protected List<IMessage> doInBackground(Void... params) {
//			return selectMessageWithNum(currentMsgPage);
//		}
//
//		@Override
//		protected void onPostExecute(List<IMessage> result) {
//			super.onPostExecute(result);
//			msgList.clear();
//			msgList.addAll(result);
//			adapter.notifyDataSetChanged();
//			chatListView.setTranscriptMode(1);
//			chatListView.setSelection(0);
//			processMessage();
//		}
//	}
//
//	CharSequence[] items = null;;
//
//	private List<IMessage> selectMessageWithNum(int page) {
//		MessageDBUtils db = MessageDBUtils.getInstance();
//		List<IMessage> result = null;
//		if (chatType == ChatType.groupchat.intValue() && !TextUtils.isEmpty(groupName)) {
//			result = db.getMessageListByGroup(groupName, page);// 添加到聊天消息
//		} else {
//			result = db.getMessageListByUser(taJid, page);
//		}
//		if (chatType == ChatType.groupchat.intValue()) {
//			MessageDBUtils.getInstance().setThreadReaded(chatType, "groupname", new String[] { groupName });
//		} else {
//			MessageDBUtils.getInstance().setThreadReaded(chatType, "tos", new String[] { taJid });
//		}
//		Collections.reverse(result);
//		return result;
//	}
//
//	private class LongClickListener implements DialogInterface.OnClickListener {
//		private int position;
//
//		public LongClickListener(int position) {
//			this.position = position;
//		}
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			IMessage message = null;
//			if (which == 0) {// 转发
//				message = msgList.get(position).copyMessage();
//				message.setSend(1);
//				ContactSelectUtil.getInstance().setMessage(message);
//				IWorkApplication.getInstance().setSendMessage(true);
//				Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//				startActivity(intent);
//			} else if (which == 1) {// 删除
//				message = msgList.remove(position);
//				MessageDBUtils.getInstance().deleteMessageById(message.getMsgid());
//				adapter.notifyDataSetChanged();
//			}
//		}
//
//	}
//
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			int rate = msg.arg1;
//			String tag = msg.obj.toString();
//			TextView tvRate = (TextView) chatListView.findViewWithTag(tag);
//			if (tvRate == null) {
//				return;
//			}
//			tvRate.setBackgroundColor(0xaadcdcdc);
//			tvRate.setText(rate + "%");
//			if (rate == 100) {
//				tvRate.setVisibility(View.GONE);
//			} else {
//				tvRate.setVisibility(View.VISIBLE);
//			}
//		}
//	};
//	private MessageDownloadManager.DownloadObserver downloadObserver = new MessageDownloadManager.DownloadObserver() {
//
//		@Override
//		public void onDownloadChange(IMessage message, int totalCount, int downSize) {
//			if (message == null) {
//				return;
//			}
//			String tag = "rate_" + message.getMsgid();
//
//			int rate = (downSize * 100) / totalCount;
//			mHandler.obtainMessage(0, rate, 0, tag).sendToTarget();
//
//		}
//	};
//	private ImageView ivRight;
//
//	@Override
//	public void onLongClickListener(int position, View view, IMessage message) {
//		items = new String[] { "转发", "删除" };
//		new AlertDialog.Builder(this).setItems(items, new LongClickListener(position)).show();
//	}
//
//}