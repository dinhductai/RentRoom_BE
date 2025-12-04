package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.Category;
import com.cntt.rentalmanagement.domain.payload.response.RoomResponse;
import com.cntt.rentalmanagement.repository.CategoryRepository;
import com.cntt.rentalmanagement.services.AIService;
import com.cntt.rentalmanagement.services.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    @Value("${google.ai.api.key:AIzaSyBckk7T1713veQ3kcLIpUaCvTOuiTu22u8}")
    private String apiKey;

    private final BlogService blogService;
    private final CategoryRepository categoryRepository;

    // Danh sách các model để thử theo thứ tự (fallback)
    // Ưu tiên các model nhanh và ổn định nhất trước
    private static final String[] GEMINI_MODELS = {
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent",
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash-lite:generateContent",
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent",
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent",
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent",
            "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent",
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"
    };

    @Override
    public String chatWithAI(String message) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo request body cho Gemini API
        Map<String, Object> requestBody = createRequestBody(message);

        // Thử từng model cho đến khi thành công
        Exception lastException = null;
        for (String apiUrl : GEMINI_MODELS) {
            try {
                System.out.println("Trying model: " + apiUrl);
                String response = callGeminiAPI(restTemplate, apiUrl, requestBody);
                if (response != null && !response.isEmpty()) {
                    return response;
                }
            } catch (org.springframework.web.client.HttpServerErrorException e) {
                // Nếu model bị overload (503), thử model tiếp theo
                if (e.getStatusCode().value() == 503) {
                    System.out.println("Model overloaded (503), trying next model...");
                    lastException = e;
                    continue;
                }
                throw e;
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                // Nếu model không tồn tại (404), thử model tiếp theo
                if (e.getStatusCode().value() == 404) {
                    System.out.println("Model not found (404), trying next model...");
                    lastException = e;
                    continue;
                }
                throw e;
            } catch (Exception e) {
                lastException = e;
                System.err.println("Error with model " + apiUrl + ": " + e.getMessage());
                continue;
            }
        }

        // Nếu tất cả model đều fail
        if (lastException != null) {
            System.err.println("All models failed. Last error: " + lastException.getMessage());
            lastException.printStackTrace();
            return "Xin lỗi, tất cả các model AI đang quá tải hoặc không khả dụng. Vui lòng thử lại sau vài phút.";
        }

        return "Xin lỗi, không thể kết nối với AI. Vui lòng thử lại sau.";
    }

    private Map<String, Object> createRequestBody(String message) {
        Map<String, Object> requestBody = new HashMap<>();

        // Lấy dữ liệu phòng trống từ database
        String roomData = getAvailableRoomsData(message);

        // Tạo prompt với dữ liệu phòng
        String prompt = buildPromptWithRoomData(message, roomData);

        // Tạo parts list
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        List<Map<String, Object>> partsList = new ArrayList<>();
        partsList.add(part);

        // Tạo content object
        Map<String, Object> contentObj = new HashMap<>();
        contentObj.put("parts", partsList);

        // Tạo contents list
        List<Map<String, Object>> contentsList = new ArrayList<>();
        contentsList.add(contentObj);

        requestBody.put("contents", contentsList);
        return requestBody;
    }

    private String callGeminiAPI(RestTemplate restTemplate, String apiUrl, Map<String, Object> requestBody) {
        try {
            // Tạo headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Tạo request entity
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Gọi API
            String url = apiUrl + "?key=" + apiKey;
            System.out.println("Calling Gemini API: " + url);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    (Class<Map<String, Object>>) (Class<?>) Map.class);

            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            System.out.println("Response body type: "
                    + (response.getBody() != null ? response.getBody().getClass().getName() : "null"));

            // Parse response
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                System.out.println("Body keys: " + body.keySet());

                // Kiểm tra error trong response
                if (body.containsKey("error")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> error = (Map<String, Object>) body.get("error");
                    String errorMessage = error.containsKey("message") ? (String) error.get("message") : "Lỗi từ API";
                    System.out.println("API Error: " + errorMessage);
                    System.out.println("Full error: " + error);
                    return "Xin lỗi, đã xảy ra lỗi: " + errorMessage;
                }

                if (body.containsKey("candidates")) {
                    Object candidates = body.get("candidates");
                    System.out.println(
                            "Candidates type: " + (candidates != null ? candidates.getClass().getName() : "null"));

                    if (candidates instanceof java.util.List) {
                        List<?> candidatesList = (List<?>) candidates;
                        System.out.println("Candidates list size: " + candidatesList.size());

                        if (!candidatesList.isEmpty()) {
                            Object firstCandidate = candidatesList.get(0);
                            System.out.println("First candidate type: " + firstCandidate.getClass().getName());
                            System.out.println("First candidate: " + firstCandidate);

                            if (firstCandidate instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> candidate = (Map<String, Object>) firstCandidate;
                                System.out.println("Candidate keys: " + candidate.keySet());

                                // Kiểm tra finishReason
                                if (candidate.containsKey("finishReason")) {
                                    String finishReason = (String) candidate.get("finishReason");
                                    System.out.println("Finish reason: " + finishReason);
                                    if (!"STOP".equals(finishReason)) {
                                        System.out.println("Warning: Finish reason is not STOP: " + finishReason);
                                    }
                                }

                                if (candidate.containsKey("content")) {
                                    Object content = candidate.get("content");
                                    System.out.println("Content type: "
                                            + (content != null ? content.getClass().getName() : "null"));

                                    if (content instanceof Map) {
                                        @SuppressWarnings("unchecked")
                                        Map<String, Object> contentMap = (Map<String, Object>) content;
                                        System.out.println("Content keys: " + contentMap.keySet());

                                        if (contentMap.containsKey("parts")) {
                                            Object parts = contentMap.get("parts");
                                            System.out.println("Parts type: "
                                                    + (parts != null ? parts.getClass().getName() : "null"));

                                            if (parts instanceof java.util.List) {
                                                List<?> responsePartsList = (List<?>) parts;
                                                System.out.println("Parts list size: " + responsePartsList.size());

                                                if (!responsePartsList.isEmpty()) {
                                                    Object firstPart = responsePartsList.get(0);
                                                    System.out.println(
                                                            "First part type: " + firstPart.getClass().getName());
                                                    System.out.println("First part: " + firstPart);

                                                    if (firstPart instanceof Map) {
                                                        @SuppressWarnings("unchecked")
                                                        Map<String, Object> partMap = (Map<String, Object>) firstPart;
                                                        System.out.println("Part keys: " + partMap.keySet());

                                                        if (partMap.containsKey("text")) {
                                                            String text = (String) partMap.get("text");
                                                            System.out.println("Extracted text: " + text);
                                                            return text;
                                                        } else {
                                                            System.out.println("Part map does not contain 'text' key");
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("Parts list is empty");
                                                }
                                            } else {
                                                System.out.println("Parts is not a List");
                                            }
                                        } else {
                                            System.out.println("Content map does not contain 'parts' key");
                                        }
                                    } else {
                                        System.out.println("Content is not a Map");
                                    }
                                } else {
                                    System.out.println("Candidate does not contain 'content' key");
                                }
                            } else {
                                System.out.println("First candidate is not a Map");
                            }
                        } else {
                            System.out.println("Candidates list is empty");
                        }
                    } else {
                        System.out.println("Candidates is not a List");
                    }
                } else {
                    System.out.println("Response body does not contain 'candidates' key");
                }
            } else {
                System.out.println("Response status is not OK or body is null");
            }

            System.out.println("Failed to parse response");
            return null; // Return null để fallback logic thử model tiếp theo
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e; // Re-throw để fallback logic có thể xử lý
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            System.err.println("HTTP Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e; // Re-throw để fallback logic có thể xử lý
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw e; // Re-throw để fallback logic có thể xử lý
        }
    }

    /**
     * Lấy dữ liệu phòng trống từ database dựa trên message của user
     */
    private String getAvailableRoomsData(String message) {
        try {
            // Parse message để tìm keyword về giá, địa điểm, loại phòng
            String title = extractTitleFromMessage(message);
            BigDecimal price = extractPriceFromMessage(message);
            Long categoryId = extractCategoryFromMessage(message);

            // Lấy danh sách phòng (tối đa 20 phòng để filter)
            Page<RoomResponse> allRooms = blogService.getAllRoomForCustomer(title, price, categoryId, 1, 20);

            if (allRooms == null || allRooms.getContent().isEmpty()) {
                return "Hiện tại không có phòng trống nào phù hợp với yêu cầu của bạn.";
            }

            // Filter chỉ lấy phòng trống (ROOM_RENT hoặc CHECKED_OUT)
            List<RoomResponse> availableRooms = new ArrayList<>();
            for (RoomResponse room : allRooms.getContent()) {
                String status = room.getStatus();
                if (status != null && (status.equals("ROOM_RENT") || status.equals("CHECKED_OUT"))) {
                    availableRooms.add(room);
                }
            }

            if (availableRooms.isEmpty()) {
                return "Hiện tại không có phòng trống nào phù hợp với yêu cầu của bạn. Tất cả các phòng đều đã được thuê.";
            }

            // Format dữ liệu phòng thành text
            StringBuilder roomData = new StringBuilder();
            roomData.append("DANH SÁCH PHÒNG TRỐNG HIỆN CÓ:\n\n");

            int index = 1;
            // Chỉ lấy tối đa 10 phòng để không quá dài
            int maxRooms = Math.min(10, availableRooms.size());
            for (int i = 0; i < maxRooms; i++) {
                RoomResponse room = availableRooms.get(i);
                roomData.append(index++).append(". ");
                roomData.append("Tiêu đề: ").append(room.getTitle() != null ? room.getTitle() : "N/A").append("\n");
                roomData.append("   Giá: ").append(room.getPrice() != null ? formatPrice(room.getPrice()) : "N/A")
                        .append("\n");
                roomData.append("   Địa chỉ: ").append(room.getAddress() != null ? room.getAddress() : "N/A")
                        .append("\n");
                if (room.getLocation() != null && room.getLocation().getCityName() != null) {
                    roomData.append("   Thành phố: ").append(room.getLocation().getCityName()).append("\n");
                }
                if (room.getCategory() != null && room.getCategory().getName() != null) {
                    roomData.append("   Loại: ").append(room.getCategory().getName()).append("\n");
                }
                if (room.getDescription() != null && !room.getDescription().isEmpty()) {
                    String desc = room.getDescription().length() > 100
                            ? room.getDescription().substring(0, 100) + "..."
                            : room.getDescription();
                    roomData.append("   Mô tả: ").append(desc).append("\n");
                }
                roomData.append("\n");
            }

            roomData.append("Tổng số: ").append(availableRooms.size()).append(" phòng trống");
            if (availableRooms.size() > 10) {
                roomData.append(" (đang hiển thị 10 phòng đầu tiên)");
            }

            return roomData.toString();
        } catch (Exception e) {
            System.err.println("Error getting room data: " + e.getMessage());
            e.printStackTrace();
            return "Không thể lấy dữ liệu phòng trống lúc này. Vui lòng thử lại sau.";
        }
    }

    /**
     * Extract title/keyword từ message
     */
    private String extractTitleFromMessage(String message) {
        // Tìm các keyword phổ biến về phòng trọ
        String lowerMessage = message.toLowerCase();
        if (lowerMessage.contains("phòng") || lowerMessage.contains("trọ") ||
                lowerMessage.contains("nhà") || lowerMessage.contains("căn hộ")) {
            // Có thể extract thêm keyword cụ thể nếu cần
            return null; // Trả về null để lấy tất cả
        }
        return null;
    }

    /**
     * Extract giá từ message (ví dụ: "dưới 5 triệu", "từ 3 đến 5 triệu", "khoảng 4
     * triệu")
     */
    private BigDecimal extractPriceFromMessage(String message) {
        try {
            String lowerMessage = message.toLowerCase();

            // Pattern để tìm số tiền (triệu, nghìn, v.v.)
            // Tìm số đầu tiên trong message
            Pattern pattern = Pattern.compile("(\\d+)\\s*(triệu|nghìn|k|tr|vnđ|đồng)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(lowerMessage);

            if (matcher.find()) {
                int number = Integer.parseInt(matcher.group(1));
                String unit = matcher.group(2).toLowerCase();

                if (unit.contains("triệu") || unit.contains("tr")) {
                    return BigDecimal.valueOf(number * 1000000); // Chuyển triệu thành VND
                } else if (unit.contains("nghìn") || unit.contains("k")) {
                    return BigDecimal.valueOf(number * 1000); // Chuyển nghìn thành VND
                } else if (unit.contains("vnđ") || unit.contains("đồng")) {
                    return BigDecimal.valueOf(number); // Đã là VND
                }
            }

            // Nếu không tìm thấy với pattern, thử tìm số đơn giản (có thể là triệu)
            Pattern simplePattern = Pattern.compile("(\\d+)\\s*(triệu|tr)", Pattern.CASE_INSENSITIVE);
            Matcher simpleMatcher = simplePattern.matcher(lowerMessage);
            if (simpleMatcher.find()) {
                int number = Integer.parseInt(simpleMatcher.group(1));
                return BigDecimal.valueOf(number * 1000000);
            }
        } catch (Exception e) {
            System.err.println("Error extracting price: " + e.getMessage());
        }
        return null;
    }

    /**
     * Extract loại phòng từ message (ví dụ: "phòng trọ", "căn hộ", "nhà nguyên
     * căn")
     */
    private Long extractCategoryFromMessage(String message) {
        try {
            String lowerMessage = message.toLowerCase();
            List<Category> categories = categoryRepository.findAll();

            for (Category category : categories) {
                if (category.getName() != null) {
                    String categoryName = category.getName().toLowerCase();
                    if (lowerMessage.contains(categoryName)) {
                        return category.getId();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting category: " + e.getMessage());
        }
        return null;
    }

    /**
     * Format giá tiền
     */
    private String formatPrice(BigDecimal price) {
        if (price == null)
            return "N/A";
        long priceLong = price.longValue();
        if (priceLong >= 1000000) {
            return (priceLong / 1000000) + " triệu VNĐ";
        } else if (priceLong >= 1000) {
            return (priceLong / 1000) + " nghìn VNĐ";
        }
        return priceLong + " VNĐ";
    }

    /**
     * Build prompt với dữ liệu phòng
     */
    private String buildPromptWithRoomData(String message, String roomData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là một trợ lý AI chuyên tư vấn về phòng trọ và bất động sản. ");
        prompt.append("Bạn có quyền truy cập vào dữ liệu thực tế về các phòng trống hiện có trong hệ thống.\n\n");
        prompt.append("DỮ LIỆU PHÒNG TRỐNG:\n");
        prompt.append(roomData);
        prompt.append("\n\n");
        prompt.append("NHIỆM VỤ CỦA BẠN:\n");
        prompt.append("1. Sử dụng dữ liệu phòng trống ở trên để trả lời câu hỏi của người dùng.\n");
        prompt.append("2. Nếu người dùng hỏi về phòng trống, giá tiền, địa điểm, hãy tham khảo dữ liệu trên.\n");
        prompt.append("3. Nếu có nhiều phòng phù hợp, hãy liệt kê một số phòng nổi bật với thông tin chi tiết.\n");
        prompt.append("4. Trả lời một cách thân thiện, hữu ích và bằng tiếng Việt.\n");
        prompt.append(
                "5. Nếu không có phòng phù hợp, hãy gợi ý các tiêu chí khác hoặc thông báo sẽ cập nhật khi có phòng mới.\n\n");
        prompt.append("CÂU HỎI CỦA NGƯỜI DÙNG: ");
        prompt.append(message);

        return prompt.toString();
    }
}
