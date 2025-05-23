@RequiresApi(api = Build.VERSION_CODES.O)
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);

    if (!NetworkUtils.isInternetAvailable(this)) {
        Intent intent = new Intent(this, NetworkCheckActivity.class);
        intent.putExtra("returnActivity", DetailWindow.class.getName());
        startActivity(intent);
        finish();
        return;
    }

    dustCheckButton = findViewById(R.id.dust_check_button);
    textViewDate = findViewById(R.id.textView_date);
    Button homeButton = findViewById(R.id.home_button); 

    // 오늘 날짜 세팅
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    textViewDate.setText(today);

    dustCheckButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDustPopup();
        }
    });

    homeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DetailWindow.this, HomeScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    });
}
